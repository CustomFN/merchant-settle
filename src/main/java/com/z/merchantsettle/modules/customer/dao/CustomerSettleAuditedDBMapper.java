package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerSettleAuditedDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerSettleAuditedDBMapper {

    CustomerSettleAuditedDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerSettleAuditedDB customerSettleAuditedDB);

    void updateByIdSelective(CustomerSettleAuditedDB customerSettleAuditedDB);

    List<CustomerSettleAuditedDB> getSettleList(@Param("customerId") Integer customerId, @Param("settleIdList") List<Integer> settleIdList);

    void deleteByCustomerId(@Param("customerId") Integer customerId);

    List<Integer> getSettleIdListByCustomerId(@Param("customerId")Integer customerId);
}
