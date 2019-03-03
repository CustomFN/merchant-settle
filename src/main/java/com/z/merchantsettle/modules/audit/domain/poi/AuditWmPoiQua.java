package com.z.merchantsettle.modules.audit.domain.poi;

import lombok.Data;

@Data
public class AuditWmPoiQua {

    private Integer recordId;
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
}
