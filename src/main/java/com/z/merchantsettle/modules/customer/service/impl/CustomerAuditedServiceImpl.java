package com.z.merchantsettle.modules.customer.service.impl;


import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerAuditedDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerAudited;
import com.z.merchantsettle.modules.customer.domain.db.CustomerAuditedDB;
import com.z.merchantsettle.modules.customer.service.CustomerAuditedService;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerAuditedServiceImpl implements CustomerAuditedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAuditedServiceImpl.class);


    @Autowired
    private CustomerAuditedDBMapper customerAuditedDBMapper;


    @Override
    @Transactional
    public void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException {
        if (customerId == null || customerId <= 0 || StringUtils.isBlank(opUserId)) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        customerAuditedDBMapper.deleteByCustomerId(customerId);
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
        CustomerAuditedDB customerAuditedDBInDB = customerAuditedDBMapper.selectById(customerAuditedDB.getId());
        if (customerAuditedDBInDB != null) {
            customerAuditedDBMapper.updateByIdSelective(customerAuditedDB);
        } else {
            customerAuditedDBMapper.insertSelective(customerAuditedDB);
        }
    }


}
