package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.ContractRequestParam;
import com.z.merchantsettle.modules.customer.domain.bo.ContractBaseInfo;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContractAudited;

import java.util.List;

public interface CustomerContractAuditedService {

    CustomerContractAudited getCustomerContractById(Integer contractId) throws CustomerException;

    void saveOrUpdate(CustomerContractAudited customerContractAudited) throws CustomerException;

    void deleteByCustomerId(Integer customerId) throws CustomerException;
}
