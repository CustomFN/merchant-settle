package com.z.merchantsettle.modules.customer.service.impl;


import com.google.common.collect.Lists;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.dao.CustomerContractAuditedDBMapper;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContractAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSignerAudited;
import com.z.merchantsettle.modules.customer.domain.db.CustomerContractAuditedDB;
import com.z.merchantsettle.modules.customer.service.CustomerContractAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerSignerAuditedService;
import com.z.merchantsettle.utils.transfer.customer.CustomerTransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerContractAuditedServiceImpl implements CustomerContractAuditedService {

    @Autowired
    private CustomerContractAuditedDBMapper customerContractAuditedDBMapper;

    @Autowired
    private CustomerSignerAuditedService customerSignerAuditedService;

    @Override
    public CustomerContractAudited getCustomerContractById(Integer contractId) throws CustomerException {
        if (contractId == null || contractId <= 0) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "参数错误");
        }
        CustomerContractAuditedDB customerContractAuditedDB = customerContractAuditedDBMapper.selectById(contractId);
        return CustomerTransferUtil.transCustomerContractAuditedDB2Bo(customerContractAuditedDB);
    }

    @Override
    public void saveOrUpdate(CustomerContractAudited customerContractAudited) throws CustomerException {
        if (customerContractAudited == null) {
            throw new CustomerException(CustomerConstant.CUSTOMER_PARAM_ERROR, "保存更新线上合同异常");
        }

        CustomerContractAuditedDB customerContractAuditedDB = CustomerTransferUtil.transCustomerContractAudited2DB(customerContractAudited);
        CustomerSignerAudited partyA = customerContractAudited.getPartyA();
        CustomerSignerAudited partyB = customerContractAudited.getPartyB();
        List<CustomerSignerAudited> customerSignerAuditedList = Lists.newArrayList(partyA, partyB);

        CustomerContractAuditedDB customerContractAuditedDBInDB = customerContractAuditedDBMapper.selectById(customerContractAuditedDB.getId());
        if (customerContractAuditedDBInDB != null) {
            customerContractAuditedDBMapper.updateByIdSelective(customerContractAuditedDB);
            customerSignerAuditedService.saveOrUpdate(customerSignerAuditedList);
        } else {
            customerContractAuditedDBMapper.insertSelective(customerContractAuditedDB);
            customerSignerAuditedService.saveOrUpdate(customerSignerAuditedList);
        }
    }

    @Override
    public void deleteByCustomerId(Integer customerId) throws CustomerException {
        if (customerId == null || customerId <= 0) {
            return;
        }
        customerContractAuditedDBMapper.deleteByCustomerId(customerId);
    }

}
