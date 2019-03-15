package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.CustomerSearchParam;
import com.z.merchantsettle.modules.customer.domain.db.CustomerAuditedDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAuditedDBMapper {

    CustomerAuditedDB selectById(@Param("id") Integer id);

    void deleteById(@Param("id") Integer id);

    void insertSelective(CustomerAuditedDB customerAuditedDB);

    void updateByIdSelective(CustomerAuditedDB customerAuditedDB);

    List<CustomerAuditedDB> getCustomerList(@Param("customerSearchParam") CustomerSearchParam customerSearchParam);

    void deleteByCustomerId(@Param("customerId") Integer customerId);
}
