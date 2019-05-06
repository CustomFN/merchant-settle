package com.z.merchantsettle.modules.poi.domain;

import lombok.Data;

@Data
public class PhysicalPoiReqParam {

    private Integer id;
    private Integer physicalCityId;
    private String physicalPoiName;
    private Integer claimed = 0;

}
