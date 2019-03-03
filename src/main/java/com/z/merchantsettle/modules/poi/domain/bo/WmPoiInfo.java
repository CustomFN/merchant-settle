package com.z.merchantsettle.modules.poi.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class WmPoiInfo {

    private Integer wmPoiId;
    private String wmPoiName;
    private String wmPoiAddress;
    private String wmPoiTel;
    private String wmPoiCategory;
    private String wmPoiPricipal;

    private List<Integer> wmPoiModuleStatus;
}
