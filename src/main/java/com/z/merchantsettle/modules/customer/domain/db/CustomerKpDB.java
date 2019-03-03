package com.z.merchantsettle.modules.customer.domain.db;

import lombok.Data;

@Data
public class CustomerKpDB {

    private Integer id;
    private Integer kpType;
    private Integer kpSiginType;
    private String kpAuthorizationPic;
    private String kpCertificatesPic;
    private String kpName;
    private Integer kpCertificatesType;
    private String kpCertificatesNum;
    private String kpPhoneNum;
    private Integer bankId;
    private String bankNum;
    private Integer status;
    private String auditResult;
    private Integer customerId;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
