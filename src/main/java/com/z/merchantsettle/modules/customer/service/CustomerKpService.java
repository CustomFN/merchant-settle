package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerKp;


public interface CustomerKpService {

    CustomerKp saveOrUpdate(CustomerKp customerKp, String opUser) throws CustomerException;

    CustomerKp getCustomerKpByCustomerId(Integer customerId, Integer effective) throws CustomerException;

    void setupEffectCustomerKp(Integer customerId) throws CustomerException;

    void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException;

    void updateByIdForAudit(CustomerKp customerKp, String opUserId);
}
