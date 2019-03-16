package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerKpDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerKpDBMapper {

    CustomerKpDB selectByCustomerId(@Param("customerId") Integer customerId);

    void deleteById(Integer id);

    void insertSelective(CustomerKpDB customerKpDB);

    void updateByIdSelective(CustomerKpDB customerKpDB);

    void updateByCustomerIdSelective(CustomerKpDB customerKpDB);

    void deleteByCustomerId(@Param("customerId") Integer customerId);
}
