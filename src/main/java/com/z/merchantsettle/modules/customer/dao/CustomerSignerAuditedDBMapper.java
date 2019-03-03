package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerSignerAuditedDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerSignerAuditedDBMapper {

    CustomerSignerAuditedDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerSignerAuditedDB customerSignerAuditedDB);

    void updateByIdSelective(CustomerSignerAuditedDB customerSignerAuditedDB);

    List<CustomerSignerAuditedDB> getCustomerSignerByContractId(@Param("contractId") Integer contractId);

    List<CustomerSignerAuditedDB> getCustomerSignerByContractIdList(@Param("contractIdList") List<Integer> contractIdList);
}
