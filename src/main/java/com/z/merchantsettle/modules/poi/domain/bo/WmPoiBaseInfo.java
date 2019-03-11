package com.z.merchantsettle.modules.poi.domain.bo;

import lombok.Data;

@Data
public class WmPoiBaseInfo {

    private Integer id;
    private Integer customerId;
    private String wmPoiName;
    private String wmPoiLinkMan;
    private String wmPoiPhone;
    private Integer wmPoiCategory;
    private Integer wmPoiCityId;
    private Integer wmPoiRegionId;
    private String wmPoiAddress;
    private String wmPoiLongitude;
    private String wmPoiLatitude;
    private String wmPoiLogo;
    private String wmPoiEnvironmentPic;
    private String wmPoiPrincipal;
    private Integer status;
    private Integer valid;
    private String auditResult;

    private String orderMealDate;
    private String orderMealTime;
    private String orderMealTel;
    private Integer businessInfoStatus;
    private String businessInfoAuditResult;
}
