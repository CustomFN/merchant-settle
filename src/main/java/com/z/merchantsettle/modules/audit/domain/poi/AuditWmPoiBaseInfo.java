package com.z.merchantsettle.modules.audit.domain.poi;

import lombok.Data;

import java.util.List;

@Data
public class AuditWmPoiBaseInfo {

    private Integer recordId;
    private Integer wmPoiId;
    private Integer customerId;
    private String wmPoiName;
    private String wmPoiLinkMan;
    private String wmPoiPhone;
    private String wmPoiCategory;
    private String wmPoiCityRegion;
    private String wmPoiAddress;
    private String wmPoiLongitude;
    private String wmPoiLatitude;
    private List<String> wmPoiLogoList;
    private List<String> wmPoiEnvironmentPicList;
    private String wmPoiPrincipal;
}
