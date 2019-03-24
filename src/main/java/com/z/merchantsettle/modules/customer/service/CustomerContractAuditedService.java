package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContractAudited;


public interface CustomerContractAuditedService {

    CustomerContractAudited getCustomerContractById(Integer contractId) throws CustomerException;

    void saveOrUpdate(CustomerContractAudited customerContractAudited) throws CustomerException;

    void deleteByCustomerId(Integer customerId) throws CustomerException;
}
