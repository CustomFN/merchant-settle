package com.z.merchantsettle.modules.customer.service.impl;

import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerKpAuditedDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerKpAudited;
import com.z.merchantsettle.modules.customer.domain.db.CustomerKpAuditedDB;
import com.z.merchantsettle.modules.customer.service.CustomerKpAuditedService;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerKpAuditedServiceImpl implements CustomerKpAuditedService {

    @Autowired
    private CustomerKpAuditedDBMapper customerKpAuditedDBMapper;

    @Override
    public CustomerKpAudited getCustomerKpByCustomerId(Integer customerId) throws CustomerException {
        if (customerId == null || customerId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }

        CustomerKpAuditedDB customerKpAuditedDB = customerKpAuditedDBMapper.selectByCustomerId(customerId);
        return CustomerTransferUtil.transCustomerKpAuditedDB2Bo(customerKpAuditedDB);
    }

    @Override
    public void saveOrUpdate(CustomerKpAudited customerKpAudited) throws CustomerException {
        if (customerKpAudited == null) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "保存更新线上KP异常");
        }

        CustomerKpAuditedDB customerKpAuditedDB = CustomerTransferUtil.transCustomerKpAudited2DB(customerKpAudited);
        if (customerKpAudited.getId() != null && customerKpAudited.getId() > 0) {
            customerKpAuditedDBMapper.updateByIdSelective(customerKpAuditedDB);
        } else {
            customerKpAuditedDBMapper.insertSelective(customerKpAuditedDB);
        }
    }
}
