package com.z.merchantsettle.modules.poi.domain;

import lombok.Data;

import java.util.List;

@Data
public class WmPoiSearchParam {

    private Integer wmPoiId;
    private Integer wmCityId;
    private String wmPoiName;
    private String wmPoiPrincipal;

}
