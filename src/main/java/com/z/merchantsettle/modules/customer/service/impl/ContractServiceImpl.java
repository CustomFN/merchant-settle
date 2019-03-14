package com.z.merchantsettle.modules.customer.service.impl;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.constants.CustomerConstant;
import com.z.merchantsettle.modules.customer.domain.ContractRequestParam;
import com.z.merchantsettle.modules.customer.domain.bo.ContractBaseInfo;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerContract;
import com.z.merchantsettle.modules.customer.service.ContractService;
import com.z.merchantsettle.modules.customer.service.CustomerContractAuditedService;
import com.z.merchantsettle.modules.customer.service.CustomerContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private CustomerContractService customerContractService;

    @Autowired
    private CustomerContractAuditedService customerContractAuditedService;


    @Override
    public PageData<ContractBaseInfo> getContractList(ContractRequestParam contractRequestParam, Integer pageNum, Integer pageSize) {
        return customerContractService.getContractBaseInfoList(contractRequestParam, pageNum, pageSize);
    }

    @Override
    public CustomerContract getContractByContractId(Integer contractId, Integer effective) throws CustomerException {
        return customerContractService.getCustomerContractById(contractId, effective);
    }
}
