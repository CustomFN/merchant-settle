package com.z.merchantsettle.modules.customer.domain.db;

import lombok.Data;

@Data
public class CustomerSettleDB {

    private Integer id;
    private Integer settleAccType;
    private String settleAccName;
    private String settleAccNo;
    private Integer province;
    private Integer city;
    private Integer bankId;
    private Integer branchId;
    private String branchName;
    private String reservePhoneNum;
    private String financialOfficer;
    private String financialOfficerPhone;
    private Integer financialOfficerCertificatesType;
    private String financialOfficerCertificatesNum;
    private Integer settleType;
    private Integer settleCycle;
    private Double settleMinAmount;
    private Integer status;
    private Long ctime;
    private Long utime;
    private Integer valid;
    private Integer customerId;
    private String auditResult;


}
