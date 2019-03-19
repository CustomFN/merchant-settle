package com.z.merchantsettle.modules.audit.domain.customer;

import lombok.Data;

@Data
public class AuditCustomerKp {

    private Integer kpId;
    private Integer customerId;
    private String kpTypeStr;
    private String kpSiginTypeStr;
    private String[] customeraAuthorizationPicArr;
    private String[] customerKpCertificatesPicArr;
    private String kpName;
    private String kpCertificatesTypeStr;
    private String kpCertificatesNum;
    private String kpPhoneNum;
    private String bankName;
    private String bankNum;

}
