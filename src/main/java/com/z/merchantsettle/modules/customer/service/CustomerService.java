package com.z.merchantsettle.modules.customer.service;


import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.CustomerSearchParam;
import com.z.merchantsettle.modules.customer.domain.bo.Customer;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerBaseInfo;

public interface CustomerService {

    PageData<CustomerBaseInfo> getCustomerList(CustomerSearchParam customerSearchParam, Integer pageNum, Integer pageSize);

    void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException;

    Customer getByCustomerId(Integer customerId, Integer effective) throws CustomerException;

    void distributePrincipal(Integer customerId, String userId, String opUserId) throws CustomerException;

    Customer saveOrUpdate(Customer customer, String opUserId) throws CustomerException;

    void setupEffectCustomer(Integer customerId) throws CustomerException;
}
