package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.ContractRequestParam;
import com.z.merchantsettle.modules.customer.domain.db.CustomerContractAuditedDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerContractAuditedDBMapper {

    CustomerContractAuditedDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerContractAuditedDB customerContractAuditedDB);

    void updateByIdSelective(CustomerContractAuditedDB customerContractAuditedDB);

    void deleteByCustomerId(@Param("customerId") Integer customerId);
}
