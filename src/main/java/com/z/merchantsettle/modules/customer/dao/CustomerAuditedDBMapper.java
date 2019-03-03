package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.CustomerSearchParam;
import com.z.merchantsettle.modules.customer.domain.db.CustomerAuditedDB;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAuditedDBMapper {

    CustomerAuditedDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerAuditedDB customerAuditedDB);

    void updateByIdSelective(CustomerAuditedDB customerAuditedDB);

    List<CustomerAuditedDB> getCustomerList(CustomerSearchParam customerSearchParam);

    void deleteByCustomerId(Integer customerId);
}
