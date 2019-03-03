package com.z.merchantsettle.modules.customer.domain.db;

import lombok.Data;

@Data
public class CustomerPoiDB {

    private Integer id;
    private Integer customerId;
    private Integer wmPoiId;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
