package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerContractDB;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerContractDBMapper {

    CustomerContractDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerContractDB customerContractDB);

    void updateByIdSelective(CustomerContractDB customerContractDB);

    CustomerContractDB selectByCustomerId(Integer customerId, Integer contractId);
}
