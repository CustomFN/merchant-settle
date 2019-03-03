package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerSettleDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerSettleDBMapper {

    CustomerSettleDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerSettleDB customerSettleDB);

    void updateByIdSelective(CustomerSettleDB customerSettleDB);

    List<CustomerSettleDB> getSettleList(@Param("settleIdList") List<Integer> settleIdList);
}
