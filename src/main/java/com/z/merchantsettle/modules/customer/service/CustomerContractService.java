package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.ContractRequestParam;
import com.z.merchantsettle.modules.customer.domain.bo.ContractBaseInfo;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContract;

import java.util.List;


public interface CustomerContractService {

    PageData<ContractBaseInfo> getCustomerContractList(Integer customerId) throws CustomerException;

    CustomerContract saveOrUpdate(CustomerContract customerContract, String opUser) throws CustomerException;

    CustomerContract getCustomerContractById(Integer contractId, Integer effective) throws CustomerException;

    void setupEffectCustomerContract(Integer customerId, Integer contractId) throws CustomerException;

    PageData<ContractBaseInfo> getContractBaseInfoList(ContractRequestParam contractRequestParam, Integer pageNum, Integer pageSize);

    void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException;

    void updateByIdForAudit(CustomerContract customerContract, String opUserId);
}
