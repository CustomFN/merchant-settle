package com.z.merchantsettle.modules.customer.service.impl;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomer;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerDBMapper;
import com.z.merchantsettle.modules.customer.dao.CustomerPoiDBMapper;
import com.z.merchantsettle.modules.customer.domain.CustomerSearchParam;
import com.z.merchantsettle.modules.customer.domain.bo.Customer;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerBaseInfo;
import com.z.merchantsettle.modules.customer.domain.db.CustomerAuditedDB;
import com.z.merchantsettle.modules.customer.domain.db.CustomerDB;
import com.z.merchantsettle.modules.customer.service.CustomerAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerOpLogService;
import com.z.merchantsettle.modules.customer.service.CustomerService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.modules.upm.service.UserService;
import com.z.merchantsettle.utils.TransferUtil;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import com.z.merchantsettle.utils.shiro.UserUtil;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerDBMapper customerDBMapper;

    @Autowired
    private CustomerPoiDBMapper customerPoiDBMapper;

    @Autowired
    private CustomerAuditedService customerAuditedService;

    @Autowired
    private ApiAuditService apiAuditService;

    @Autowired
    private CustomerOpLogService customerOpLogService;

    @Autowired
    private UserUtil userUtil;

    @Override
    public PageData<CustomerBaseInfo> getCustomerList(CustomerSearchParam customerSearchParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerDB> customerDBList = customerDBMapper.getCustomerList(customerSearchParam);
        PageInfo<CustomerDB> pageInfo = new PageInfo<>(customerDBList);

        List<CustomerBaseInfo> customerBaseInfoList = Lists.newArrayList();
        for (CustomerDB customerDB : customerDBList) {
            CustomerBaseInfo customerBaseInfo = new CustomerBaseInfo();
            customerBaseInfo.setCustomerId(customerDB.getId());
            customerBaseInfo.setCustomerName(customerDB.getCustomerName());
            customerBaseInfo.setCustomerType(CustomerConstant.CustomerTypeEnum.getByCode(customerDB.getCustomerType()));
            customerBaseInfo.setCustomerStatus(CustomerConstant.CustomerStatus.getByCode(customerDB.getStatus()));

            User user = userUtil.getUser(customerDB.getCustomerPrincipal());
            if (user != null) {
                customerBaseInfo.setCustomerPrincipal(user.getUserName());
            }

            int count = customerPoiDBMapper.selectCountByCustomerId(customerDB.getId());
            customerBaseInfo.setCustomerPoiRelNum(count);
            customerBaseInfoList.add(customerBaseInfo);
        }
        return new PageData.Builder<CustomerBaseInfo>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPages())
                .data(customerBaseInfoList)
                .build();
    }

    @Override
    public void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException {
        if (customerId == null || customerId <= 0 ) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        customerDBMapper.deleteByCustomerId(customerId);
    }

    @Override
    public Customer getByCustomerId(Integer customerId, Integer effective) throws CustomerException {
        if (customerId == null || customerId <= 0 || effective < 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        Customer customer;
        if (CustomerConstant.EFFECTIVE == effective) {
            CustomerAudited customerAudited = customerAuditedService.selectById(customerId);
            customer = transCustomerAudited2Customer(customerAudited);
        } else {
            customer = getByCustomerId(customerId);
        }
        return customer;
    }

    @Override
    public void distributePrincipal(Integer customerId, String userId, String opUserId) throws CustomerException {
        if (customerId == null || customerId <= 0 || StringUtils.isBlank(userId)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerDB customerDB = new CustomerDB();
        customerDB.setId(customerId);
        customerDB.setCustomerPrincipal(userId);
        customerDBMapper.updateByIdSelective(customerDB);

        CustomerAudited customerAudited = new CustomerAudited();
        customerAudited.setId(customerId);
        customerAudited.setCustomerPrincipal(userId);
        customerAuditedService.updateByIdSelective(customerAudited);
    }

    @Override
    public Customer saveOrUpdate(Customer customer, String opUserId) throws CustomerException {
        LOGGER.info("saveOrUpdate customer = {}, opUserId = {}", JSON.toJSONString(customer), opUserId);
        if (customer == null || StringUtils.isBlank(opUserId)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerDB customerDB = CustomerTransferUtil.transCustomer2DB(customer);
        boolean isNew = (null == customer.getId() || customerDB.getId() <= 0);
        if (isNew) {
            customerDB.setStatus(CustomerConstant.CustomerStatus.AUDITING.getCode());
            customerDB.setCustomerPrincipal(opUserId);
            customerDBMapper.insertSelective(customerDB);

            customer = CustomerTransferUtil.transCustomerDB2Bo(customerDB);
        } else {
            customerDBMapper.updateByIdSelective(customerDB);
        }

        commitAudit(customerDB, opUserId, isNew);
        customerOpLogService.addLog(customerDB.getId(), "KP", "保存客户，提交审核", opUserId);
        return customer;
    }

    private void commitAudit(CustomerDB customerDB, String opUserId, boolean isNew) {
        AuditTask auditTask = new AuditTask();
        auditTask.setCustomerId(customerDB.getId());
        auditTask.setAuditApplicationType(isNew ? AuditConstant.AuditApplicationType.AUDIT_NEW : AuditConstant.AuditApplicationType.AUDIT_UPDATE);
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditConstant.AuditType.CUSTOMER);
        auditTask.setSubmitterId(opUserId);

        AuditCustomer auditCustomer = new AuditCustomer();
        TransferUtil.transferAll(customerDB, auditCustomer);
        auditTask.setAuditData(JSON.toJSONString(auditCustomer));
        apiAuditService.commitAudit(auditTask);
    }


    private Customer transCustomerAudited2Customer(CustomerAudited customerAudited) {
        if (customerAudited == null) {
            return null;
        }

        Customer customer = new Customer();
        TransferUtil.transferAll(customerAudited, customer);
        return customer;
    }

    public Customer getByCustomerId(Integer customerId) throws CustomerException {
        if (customerId == null || customerId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerDB customerDB = customerDBMapper.selectById(customerId);
        return CustomerTransferUtil.transCustomerDB2Bo(customerDB);
    }

    @Override
    @Transactional
    public void setupEffectCustomer(Integer customerId) throws CustomerException {
        LOGGER.info("setupEffectCustomer customerId = {}", customerId);

        CustomerDB customerDB = customerDBMapper.selectById(customerId);
        if (customerDB == null) {
            return;
        }

        customerDB.setStatus(CustomerConstant.CustomerStatus.AUDIT_PASS.getCode());
        customerDBMapper.updateByIdSelective(customerDB);

        Customer customer = CustomerTransferUtil.transCustomerDB2Bo(customerDB);
        CustomerAudited customerAudited = transCustomer2CustomerAudited(customer);
        customerAuditedService.saveOrUpdate(customerAudited);

        customerOpLogService.addLog(customerId, "客户", "客户生效", "系统()");
    }

    private CustomerAudited transCustomer2CustomerAudited(Customer customer) {
        CustomerAudited customerAudited = new CustomerAudited();
        TransferUtil.transferAll(customer, customerAudited);
        return customerAudited;
    }
}
