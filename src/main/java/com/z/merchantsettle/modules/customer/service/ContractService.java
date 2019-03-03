package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.ContractRequestParam;
import com.z.merchantsettle.modules.customer.domain.bo.ContractBaseInfo;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContract;

public interface ContractService {

    PageData<ContractBaseInfo> getContractList(ContractRequestParam contractRequestParam, Integer pageNum, Integer pageSize);

    CustomerContract getContractByContractId(Integer contractId, Integer effective) throws CustomerException;
}
