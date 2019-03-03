package com.z.merchantsettle.modules.poi.domain.db;

import lombok.Data;

@Data
public class WmPoiQuaDB {

    private Integer id;
    private Integer wmPoiId;

    private String wmPoiLinkManIDCardPic;
    private Integer wmPoiLinkManIDCardType;
    private String wmPoiLinkManName;
    private String wmPoiLinkManIDCardNo;

    private String wmPoiBusinessLicencePic;
    private String wmPoiBusinessLicenceNo;
    private String wmPoiOperatorName;
    private String wmPoiBusinessLicenceName;
    private String wmPoiBusinessLicenceAddress;
    private String wmPoiRegistrationDate;
    private String wmPoiRegisterDepartment;
    private String wmPoiBusinessLicenceValidTime;

    private Integer status;
    private Integer valid;
    private String auditResult;

    private Long ctime;
    private Long utime;

}
