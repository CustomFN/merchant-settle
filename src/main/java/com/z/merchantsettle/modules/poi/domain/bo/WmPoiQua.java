package com.z.merchantsettle.modules.poi.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class WmPoiQua {

    private Integer id;
    private Integer wmPoiId;

    private String wmPoiLinkManIDCardPic;
    private List<String> wmPoiLinkManIDCardPicList;
    private Integer wmPoiLinkManIDCardType;
    private String wmPoiLinkManName;
    private String wmPoiLinkManIDCardNo;

    private String wmPoiBusinessLicencePic;
    private List<String> wmPoiBusinessLicencePicList;
    private String wmPoiBusinessLicenceNo;
    private String wmPoiOperatorName;
    private String wmPoiBusinessLicenceName;
    private String wmPoiBusinessLicenceAddress;
    private String wmPoiRegistrationDate;
    private String wmPoiRegisterDepartment;
    private Long wmPoiBusinessLicenceValidTime;

    private Integer status;
    private String statusStr;
    private Integer valid;
    private String auditResult;

}
