package com.z.merchantsettle.modules.poi.domain.bo;

import lombok.Data;

import java.util.List;

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
    private List<String> wmPoiLogoList;
    private String wmPoiEnvironmentPic;
    private List<String> wmPoiEnvironmentPicList;
    private String wmPoiPrincipal;
    private Integer status;
    private String statusStr;
    private Integer valid;
    private String auditResult;

    private String orderMealDate;
    private List<String> orderMealDateList;
    private String orderMealStartTime;
    private String orderMealEndTime;
    private String orderMealTel;
    private Integer businessInfoStatus;
    private String businessInfoStatusStr;
    private String businessInfoAuditResult;

    private Integer coopState;
}
