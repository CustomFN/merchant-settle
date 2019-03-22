package com.z.merchantsettle.modules.audit.domain.poi;

import lombok.Data;

import java.util.List;

@Data
public class AuditWmPoiQua {

    private Integer recordId;
    private Integer wmPoiId;

    private List<String> wmPoiLinkManIDCardPicList;
    private String wmPoiLinkManIDCardType;
    private String wmPoiLinkManName;
    private String wmPoiLinkManIDCardNo;

    private List<String> wmPoiBusinessLicencePicList;
    private String wmPoiBusinessLicenceNo;
    private String wmPoiOperatorName;
    private String wmPoiBusinessLicenceName;
    private String wmPoiBusinessLicenceAddress;
    private String wmPoiRegistrationDate;
    private String wmPoiRegisterDepartment;
    private String wmPoiBusinessLicenceValidTime;
}
