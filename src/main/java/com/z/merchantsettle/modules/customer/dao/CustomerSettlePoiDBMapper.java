package com.z.merchantsettle.modules.customer.dao;

import com.z.merchantsettle.modules.customer.domain.db.CustomerSettlePoiDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerSettlePoiDBMapper {

    List<Integer> getSettleIdByWmPoiId(Integer wmPoiId);

    List<CustomerSettlePoiDB> getBySettleId(Integer settleId);

    void deleteBySettleIdAndPoiIdList(Integer settleId, @Param("wmPoiIdList") List<Integer> wmPoiIdList);

    void save(@Param("list") List<CustomerSettlePoiDB> CustomerSettlePoiDBList);

    List<CustomerSettlePoiDB> getByWmPoiIdList(@Param("wmPoiIdList") List<Integer> wmPoiIdList);
}
