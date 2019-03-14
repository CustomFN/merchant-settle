package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.CustomerSearchParam;
import com.z.merchantsettle.modules.customer.domain.db.CustomerDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDBMapper {

    CustomerDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerDB customerDB);

    void updateByIdSelective(CustomerDB customerDB);

    void deleteByCustomerId(Integer customerId);

    List<CustomerDB> getCustomerList(@Param("customerSearchParam") CustomerSearchParam customerSearchParam);
}
