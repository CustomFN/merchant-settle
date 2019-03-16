package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.ContractRequestParam;
import com.z.merchantsettle.modules.customer.domain.db.CustomerContractDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerContractDBMapper {

    CustomerContractDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerContractDB customerContractDB);

    void updateByIdSelective(CustomerContractDB customerContractDB);

    CustomerContractDB selectByCustomerId(Integer customerId, Integer contractId);

    List<CustomerContractDB> getByCustomerId(@Param("customerId") Integer customerId);

    List<CustomerContractDB> getContractList(@Param("contractRequestParam") ContractRequestParam contractRequestParam);

    void deleteByCustomerId(@Param("customerId") Integer customerId);
}
