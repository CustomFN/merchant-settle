package com.z.merchantsettle.modules.poi.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class WmPoiQua {

    private Integer id;
    private Integer wmPoiId;

    private String wmPoiLinkManIDCardPic;

    @NotNull(message = "联系人证件图片不能为空")
    private List<String> wmPoiLinkManIDCardPicList;
    private Integer wmPoiLinkManIDCardType;

    @NotBlank(message = "联系人名称不能为空")
    private String wmPoiLinkManName;

    @NotBlank(message = "联系人证件号码不能为空")
    private String wmPoiLinkManIDCardNo;

    private String wmPoiBusinessLicencePic;

    @NotNull(message = "营业执照图片不能为空")
    private List<String> wmPoiBusinessLicencePicList;

    @NotBlank(message = "营业执照号码不能为空")
    private String wmPoiBusinessLicenceNo;

    @NotBlank(message = "经营者姓名不能为空")
    private String wmPoiOperatorName;

    @NotBlank(message = "营业执照门店名称不能为空")
    private String wmPoiBusinessLicenceName;

    @NotBlank(message = "营业执照门店地址不能为空")
    private String wmPoiBusinessLicenceAddress;

    @NotBlank(message = "营业执照门店注册日期不能为空")
    private String wmPoiRegistrationDate;

    @NotBlank(message = "营业执照门店登记机关不能为空")
    private String wmPoiRegisterDepartment;

    @NotBlank(message = "营业执照有效期不能为空")
    private Long wmPoiBusinessLicenceValidTime;

    private Integer status;
    private String statusStr;
    private Integer valid;
    private String auditResult;

}
