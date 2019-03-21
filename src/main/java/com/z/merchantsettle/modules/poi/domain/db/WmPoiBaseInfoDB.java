package com.z.merchantsettle.modules.poi.domain.db;

import lombok.Data;

@Data
public class WmPoiBaseInfoDB {

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
    private Long ctime;
    private Long utime;
    private Integer valid;
    private String auditResult;

    private String orderMealDate;
    private String orderMealStartTime;
    private String orderMealEndTime;
    private String orderMealTel;
    private Integer businessInfoStatus;
    private String businessInfoAuditResult;

    private Integer coopState;
}
