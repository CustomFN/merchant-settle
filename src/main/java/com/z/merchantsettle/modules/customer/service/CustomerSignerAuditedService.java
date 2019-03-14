package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSignerAudited;
import com.z.merchantsettle.modules.customer.domain.db.CustomerSignerAuditedDB;

import java.util.List;
import java.util.Map;

public interface CustomerSignerAuditedService {

    List<CustomerSignerAudited> getCustomerSignerByContractId(Integer contractId) throws CustomerException;

    void saveOrUpdate(List<CustomerSignerAudited> customerSignerAuditedList) throws CustomerException;

}
