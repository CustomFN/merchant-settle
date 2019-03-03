package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerDB;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDBMapper {

    CustomerDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerDB customerDB);

    void updateByIdSelective(CustomerDB customerDB);

    void deleteByCustomerId(Integer customerId);

}
