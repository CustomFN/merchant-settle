package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class CustomerPoi {

    private Integer id;
    private Integer customerId;
    private Integer wmPoiId;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
