package com.z.merchantsettle.modules.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerAuditedDBMapper;
import com.z.merchantsettle.modules.customer.dao.CustomerDBMapper;
import com.z.merchantsettle.modules.customer.dao.CustomerPoiDBMapper;
import com.z.merchantsettle.modules.customer.domain.CustomerSearchParam;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerBaseInfo;
import com.z.merchantsettle.modules.customer.domain.db.CustomerAuditedDB;
import com.z.merchantsettle.modules.customer.service.CustomerAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerOpLogService;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerAuditedServiceImpl implements CustomerAuditedService {

    @Autowired
    private CustomerDBMapper customerDBMapper;

    @Autowired
    private CustomerAuditedDBMapper customerAuditedDBMapper;

    @Autowired
    private CustomerPoiDBMapper customerPoiDBMapper;

    @Autowired
    private CustomerOpLogService customerOpLogService;

    @Override
    public PageData<CustomerBaseInfo> getCustomerList(CustomerSearchParam customerSearchParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CustomerAuditedDB> customerAuditedDBList = customerAuditedDBMapper.getCustomerList(customerSearchParam);
        PageInfo<CustomerAuditedDB> pageInfo = new PageInfo<>(customerAuditedDBList);

        List<CustomerBaseInfo> customerBaseInfoList = Lists.newArrayList();
        for (CustomerAuditedDB customerAuditedDB : customerAuditedDBList) {
            CustomerBaseInfo customerBaseInfo = new CustomerBaseInfo();
            customerBaseInfo.setCustomerId(customerAuditedDB.getId());
            customerBaseInfo.setCustomerName(customerAuditedDB.getCustomerName());
            customerBaseInfo.setCustomerType(CustomerConstant.CustomerTypeEnum.getByCode(customerAuditedDB.getCustomerType()));
            customerBaseInfo.setCustomerStatus(CustomerConstant.CustomerStatus.getByCode(customerAuditedDB.getStatus()));

            int count = customerPoiDBMapper.selectCountByCustomerId(customerAuditedDB.getId());
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
    public void deleteByCustomerId(Integer customerId, String opUser) throws CustomerException {
        if (customerId == null || customerId <= 0 || StringUtils.isBlank(opUser)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        customerDBMapper.deleteByCustomerId(customerId);
        customerAuditedDBMapper.deleteByCustomerId(customerId);

        customerOpLogService.addLog(customerId, "客户", "删除客户", opUser);
    }

    @Override
    public CustomerAudited selectById(Integer customerId) throws CustomerException {
        if (customerId == null || customerId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerAuditedDB customerAuditedDB = customerAuditedDBMapper.selectById(customerId);
        return CustomerTransferUtil.transCustomerAuditedDB2Bo(customerAuditedDB);
    }

    @Override
    public void updateByIdSelective(CustomerAudited customerAudited) throws CustomerException {
        if (customerAudited == null || customerAudited.getId() <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        customerAuditedDBMapper.updateByIdSelective(CustomerTransferUtil.transCustomerAudited2DB(customerAudited));
    }

    @Override
    public void saveOrUpdate(CustomerAudited customerAudited) throws CustomerException {
        if (customerAudited == null) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "保存线上客户异常");
        }

        CustomerAuditedDB customerAuditedDB = CustomerTransferUtil.transCustomerAudited2DB(customerAudited);
        if (customerAudited.getId() != null && customerAudited.getId() > 0) {
            customerAuditedDBMapper.updateByIdSelective(customerAuditedDB);
        } else {
            customerAuditedDBMapper.insertSelective(customerAuditedDB);
        }
    }


}
