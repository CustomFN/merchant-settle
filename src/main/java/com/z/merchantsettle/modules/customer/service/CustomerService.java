package com.z.merchantsettle.modules.customer.service;


import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.bo.Customer;

public interface CustomerService {


    void deleteByCustomerId(Integer customerId, String opUserId) throws CustomerException;

    Customer getByCustomerId(Integer customerId, Integer effective) throws CustomerException;

    void distributePrincipal(Integer customerId, String userId, String opUserId) throws CustomerException;

    void saveOrUpdate(Customer customer, String opUserId) throws CustomerException;

    void setupEffectCustomer(Integer customerId) throws CustomerException;
}
