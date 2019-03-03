package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSigner;

import java.util.List;

public interface CustomerSignerService {

    void updateByIdSelective(List<CustomerSigner> customerSignerList) throws CustomerException;

    void insertSelective(List<CustomerSigner> customerSignerList) throws CustomerException;

    List<CustomerSigner> getCustomerSignerByContractId(Integer contractId) throws CustomerException;
}
