package com.z.merchantsettle.modules.audit.domain.poi;

import lombok.Data;

import java.util.List;

@Data
public class AuditWmPoiProject {

    private Integer wmPoiId;

    private Double minDeliveryPrice;
    private Double dispatcherMoney;

    private Integer deliveryAreaType;
    private List<Double> deliveryArea;
}
