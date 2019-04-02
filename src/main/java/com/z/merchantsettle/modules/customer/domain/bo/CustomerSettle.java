package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CustomerSettle {

    private Integer id;
    private Integer settleAccType;

    @NotBlank(message = "账户名称不能为空")
    private String settleAccName;

    @NotBlank(message = "账户号码不能为空")
    private String settleAccNo;
    private Integer province;
    private Integer city;
    private Integer bankId;
    private Integer branchId;
    private String branchName;

    @NotBlank(message = "银行预留手机号不能为空")
    private String reservePhoneNum;

    @NotBlank(message = "财务负责人不能为空")
    private String financialOfficer;

    @NotBlank(message = "财务负责人手机号不能为空")
    private String financialOfficerPhone;
    private Integer financialOfficerCertificatesType;

    @NotBlank(message = "财务负责人证件号不能为空")
    private String financialOfficerCertificatesNum;
    private Integer settleType;
    private Integer settleCycle;
    private Double settleMinAmount;
    private Integer status;
    private String statusStr;
    private Long ctime;
    private Long utime;
    private Integer valid;
    private Integer customerId;
    private String auditResult;
    private String wmPoiIds;
}
