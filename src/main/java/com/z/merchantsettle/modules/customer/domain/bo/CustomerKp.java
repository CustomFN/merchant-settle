package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CustomerKp {

    private Integer id;
    private Integer kpType;
    private Integer kpSiginType;
    private String kpAuthorizationPic;
    private List<String> kpAuthorizationPicList;
    private String kpCertificatesPic;

    @NotEmpty(message = "客户KP证件图片不能为空")
    private List<String> kpCertificatesPicList;

    @NotBlank(message = "客户KP姓名不能为空")
    private String kpName;
    private Integer kpCertificatesType;

    @NotBlank(message = "客户KP证件号不能为空")
    private String kpCertificatesNum;

    @NotBlank(message = "客户KP手机号不能为空")
    private String kpPhoneNum;
    private Integer bankId;
    private String bankNum;
    private Integer status;
    private String statusStr;
    private Integer customerId;
    private String auditResult;
    private Long ctime;
    private Long utime;
    private Integer valid;

}
