package com.z.merchantsettle.modules.customer.domain.db;

import lombok.Data;

@Data
public class CustomerSettlePoiDB {

    private Integer id;
    private Integer settleId;
    private Integer wmPoiId;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
