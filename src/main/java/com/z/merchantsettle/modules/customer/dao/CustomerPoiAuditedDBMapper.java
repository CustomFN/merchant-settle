package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerPoiDB;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPoiAuditedDBMapper {

    List<CustomerPoiDB> selectByCustomerId(Integer customerId);


    void unbindCustomerPoi(Integer customerId, Integer wmPoiId);

    void unbindCustomerPoiAll(Integer customerId);

    void insert(CustomerPoiDB customerPoiDB);

    Integer selectCountByCustomerId(Integer customerId);
}
