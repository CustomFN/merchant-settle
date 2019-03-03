package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class CustomerSettlePoi {

    private Integer id;
    private Integer settleId;
    private Integer wmPoiId;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
