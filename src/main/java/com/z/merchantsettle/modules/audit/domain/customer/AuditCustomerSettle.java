package com.z.merchantsettle.modules.audit.domain.customer;

import lombok.Data;

import java.util.List;

@Data
public class AuditCustomerSettle {

    private Integer settleId;

    private String settleAccTypeStr;
    private String settleAccName;
    private String settleAccNo;
    private String bankName;
    private String reservePhoneNum;
    private String financialOfficer;
    private String financialOfficerPhone;
    private String financialOfficerCertificatesTypeStr;
    private String financialOfficerCertificatesNum;
    private String settleTypeStr;
    private String settleCycleStr;
    private Double settleMinAmount;
    private String wmPoiIds;
}
