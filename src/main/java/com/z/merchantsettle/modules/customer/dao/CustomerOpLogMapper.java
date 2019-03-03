package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.CustomerOpLog;
import com.z.merchantsettle.modules.customer.domain.CustomerOpLogSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOpLogMapper {

    void insert(@Param("customerOpLog") CustomerOpLog customerOpLog);

    List<CustomerOpLog> getLogByCustomerId(@Param("opLogSearchParam") CustomerOpLogSearchParam opLogSearchParam);
}
