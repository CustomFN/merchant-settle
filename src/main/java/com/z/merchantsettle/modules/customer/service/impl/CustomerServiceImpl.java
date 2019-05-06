package com.z.merchantsettle.modules.customer.service.impl;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.audit.constants.AuditApplicationTypeEnum;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomer;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.constants.CustomerTypeEnum;
import com.z.merchantsettle.modules.customer.dao.CustomerDBMapper;
import com.z.merchantsettle.modules.customer.dao.CustomerPoiDBMapper;
import com.z.merchantsettle.modules.customer.domain.CustomerSearchParam;
import com.z.merchantsettle.modules.customer.domain.bo.Customer;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerBaseInfo;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContract;
import com.z.merchantsettle.modules.customer.domain.db.CustomerAuditedDB;
import com.z.merchantsettle.modules.customer.domain.db.CustomerDB;
import com.z.merchantsettle.modules.customer.service.CustomerAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerOpLogService;
import com.z.merchantsettle.modules.customer.service.CustomerService;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.modules.upm.service.UserService;
import com.z.merchantsettle.mq.MsgOpType;
import com.z.merchantsettle.mq.customer.CustomerSender;
import com.z.merchantsettle.utils.DiffUtil;
import com.z.merchantsettle.utils.TransferUtil;
import com.z.merchantsettle.utils.aliyun.AliyunUtil;
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

    @Autowired
    private CustomerSender customerSender;

    private static final String CUSTOMER_TOPIC = "customer_topic";

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
            customerBaseInfo.setCustomerType(CustomerTypeEnum.getByCode(customerDB.getCustomerType()));
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
    @Transactional
    public void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException {
        LOGGER.info("CustomerServiceImpl deleteByCustomerId customerId = {}, opUser = {}", customerId, opUserId);
        if (customerId == null || customerId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        customerDBMapper.deleteByCustomerId(customerId);
        customerAuditedService.deleteByCustomerId(customerId, opUserId);
        customerOpLogService.addLog(customerId, "客户", "删除客户", opUserId);

        customerSender.send(CUSTOMER_TOPIC, customerId, MsgOpType.DELETE);
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
    public void distributePrincipal(Integer customerId, String customerPrincipal, String opUserId) throws CustomerException {
        if (customerId == null || customerId <= 0 || StringUtils.isBlank(customerPrincipal)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerDB customerDB = new CustomerDB();
        customerDB.setId(customerId);
        customerDB.setCustomerPrincipal(customerPrincipal);
        customerDBMapper.updateByIdSelective(customerDB);

        CustomerAudited customerAudited = new CustomerAudited();
        customerAudited.setId(customerId);
        customerAudited.setCustomerPrincipal(customerPrincipal);
        customerAuditedService.updateByIdSelective(customerAudited);
    }

    @Override
    @Transactional
    public Customer saveOrUpdate(Customer customer, String opUserId) throws CustomerException {
        LOGGER.info("saveOrUpdate customer = {}, opUserId = {}", JSON.toJSONString(customer), opUserId);
        if (customer == null || StringUtils.isBlank(opUserId)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        if (false) {
            checkCustomer(customer);
        }


        CustomerDB customerDB = CustomerTransferUtil.transCustomer2DB(customer);
        boolean isNew = (null == customer.getId() || customerDB.getId() <= 0);
        if (isNew) {
            customerDB.setStatus(CustomerConstant.CustomerStatus.AUDITING.getCode());
            customerDB.setCustomerPrincipal(opUserId);
            customerDBMapper.insertSelective(customerDB);

            customer = CustomerTransferUtil.transCustomerDB2Bo(customerDB);
            String diff = DiffUtil.diff(null, customer);
            customerOpLogService.addLog(customerDB.getId(), "客户", diff, opUserId);
        } else {
            CustomerDB customerDBInDB = customerDBMapper.selectById(customerDB.getId());
            customerDBMapper.updateByIdSelective(customerDB);

            String diff = DiffUtil.diff(customerDBInDB, customerDB);
            customerOpLogService.addLog(customerDB.getId(), "客户", diff, opUserId);
        }

        commitAudit(customer, opUserId, isNew);
        customerOpLogService.addLog(customerDB.getId(), "客户", "保存客户，提交审核", opUserId);
        return customer;
    }

    private void checkCustomer(Customer customer) {
        boolean validResult = true;
        if (CustomerTypeEnum.CUSTOMER_TYPE_ENTERPRISE.getCode() == customer.getCustomerType()) {
            validResult = AliyunUtil.enterpriseValid(customer.getCustomerCertificatesNum(), customer.getCustomerName(), customer.getCustomerLegalPerson());
        } else {
            validResult = AliyunUtil.iDCardValid(customer.getCustomerCertificatesNum(), customer.getCustomerName());
        }

        if (!validResult) {
            throw new CustomerException(CustomerConstant.CUSTOMER_VALID_ERROR, "客户资质验证失败,请重新确认!");
        }
    }

    private void commitAudit(Customer customer, String opUserId, boolean isNew) {
        AuditTask auditTask = new AuditTask();
        auditTask.setCustomerId(customer.getId());
        auditTask.setAuditApplicationType(isNew ? AuditApplicationTypeEnum.AUDIT_NEW.getCode() : AuditApplicationTypeEnum.AUDIT_UPDATE.getCode());
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditTypeEnum.CUSTOMER.getCode());
        auditTask.setSubmitterId(opUserId);

        AuditCustomer auditCustomer = new AuditCustomer();
        TransferUtil.transferAll(customer, auditCustomer);

        String customerCertificatesPic = customer.getCustomerCertificatesPic();
        String[] picArr = StringUtils.split(customerCertificatesPic, ",");
        auditCustomer.setCustomerCertificatesPicArr(picArr);
        auditCustomer.setCustomerTypeStr(CustomerTypeEnum.getByCode(customer.getCustomerType()));

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

    @Override
    public void updateByIdForAudit(Customer customer, String opUserId) {
        LOGGER.info("updateByIdForAudit customer = {}, opUserId = {}", JSON.toJSONString(customer), opUserId);
        if (customer == null || StringUtils.isBlank(opUserId)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerDB customerDB = customerDBMapper.selectById(customer.getId());
        if (customerDB == null) {
            throw new CustomerException(CustomerConstant.CUSTOMER_OP_ERROR, "更新客户审核状态异常");
        }

        customerDB.setStatus(customer.getStatus());
        customerDB.setAuditResult(customer.getAuditResult());
        customerDBMapper.updateByIdSelective(customerDB);

        String log = "审核结果:审核驳回:" + customerDB.getAuditResult();
        customerOpLogService.addLog(customerDB.getId(), "客户", log, "系统()");
    }
}
