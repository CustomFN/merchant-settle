package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContract;


public interface CustomerContractService {
    void saveOrUpdate(CustomerContract customerContract, String opUser) throws CustomerException;

    CustomerContract getCustomerContractById(Integer contractId, Integer effective) throws CustomerException;

    void setupEffectCustomerContract(Integer customerId, Integer contractId) throws CustomerException;
}
