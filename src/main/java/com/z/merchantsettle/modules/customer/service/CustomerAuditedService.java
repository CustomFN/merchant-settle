package com.z.merchantsettle.modules.customer.service;

import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.modules.customer.domain.CustomerSearchParam;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerAudited;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerBaseInfo;

public interface CustomerAuditedService {

    PageData<CustomerBaseInfo> getCustomerList(CustomerSearchParam customerSearchParam, Integer pageNum, Integer pageSize);

    void deleteByCustomerId(Integer customerId, String opUser) throws CustomerException;


    CustomerAudited selectById(Integer customerId) throws CustomerException;

    void updateByIdSelective(CustomerAudited customerAudited) throws CustomerException;

    void saveOrUpdate(CustomerAudited customerAudited) throws CustomerException;
}
