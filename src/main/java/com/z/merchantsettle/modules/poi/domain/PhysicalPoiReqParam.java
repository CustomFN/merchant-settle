package com.z.merchantsettle.modules.poi.domain;

import lombok.Data;

@Data
public class PhysicalPoiReqParam {

    private Integer physicalPoiId;
    private Integer physicalCityId;
    private String physicalPoiName;
    private Integer claimed;

}
