package com.z.merchantsettle.modules.audit.domain.customer;

import lombok.Data;

import java.util.List;

@Data
public class AuditCustomerSettle {

    private Integer settleId;

    private Integer settleAccType;
    private String settleAccName;
    private String settleAccNo;
    private String bankName;
    private String reservePhoneNum;
    private String financialOfficer;
    private String financialOfficerPhone;
    private Integer financialOfficerCertificatesType;
    private String financialOfficerCertificatesNum;
    private Integer settleType;
    private Integer settleCycle;
    private Double settleMinAmount;
    List<String> wmPoiName;
}
