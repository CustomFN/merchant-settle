package com.z.merchantsettle.modules.audit.domain.customer;

import lombok.Data;

@Data
public class AuditCustomerKp {

    private Integer kpId;
    private Integer customerId;
    private Integer kpType;
    private Integer kpSiginType;
    private String customeraAuthorizationPic;
    private String customerKpCertificatesPic;
    private String kpName;
    private Integer kpCertificatesType;
    private String kpCertificatesNum;
    private String kpPhoneNum;
    private String bankName;
    private String bankNum;

}
