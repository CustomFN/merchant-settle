package com.z.merchantsettle.modules.poi.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class WmPoiProject {

    private Integer wmPoiId;

    private Double minDeliveryPrice;
    private Double dispatcherMoney;

    private Integer deliveryAreaType;
    private List<Double> deliveryArea;
}
