package com.z.merchantsettle.modules.customer.service.impl;

import com.alibaba.fastjson.JSON;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerAuditedServiceImpl implements CustomerAuditedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAuditedServiceImpl.class);

    @Autowired
    private CustomerDBMapper customerDBMapper;

    @Autowired
    private CustomerAuditedDBMapper customerAuditedDBMapper;

    @Autowired
    private CustomerPoiDBMapper customerPoiDBMapper;

    @Autowired
    private CustomerOpLogService customerOpLogService;

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
