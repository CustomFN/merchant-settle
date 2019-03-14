package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerSignerDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerSignerDBMapper {

    CustomerSignerDB selectById(Integer id);

    void deleteById(Integer id);

    void insertSelective(CustomerSignerDB customerSignerDB);

    void updateByIdSelective(CustomerSignerDB customerSignerDB);

    List<CustomerSignerDB> getCustomerSignerByContractId(@Param("contractId") Integer contractId);

    List<CustomerSignerDB> getCustomerSignerByContractIdList(@Param("contractIdList") List<Integer> contractIdList);

}
