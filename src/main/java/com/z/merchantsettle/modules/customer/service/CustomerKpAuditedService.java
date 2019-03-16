package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerKpAudited;

public interface CustomerKpAuditedService {
    CustomerKpAudited getCustomerKpByCustomerId(Integer customerId) throws CustomerException;

    void saveOrUpdate(CustomerKpAudited customerKpAudited) throws CustomerException;

    void deleteByCustomerId(Integer customerId) throws CustomerException;
}
