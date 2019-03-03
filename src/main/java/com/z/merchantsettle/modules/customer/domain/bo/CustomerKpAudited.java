package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class CustomerKpAudited {

    private Integer id;
    private Integer kpType;
    private Integer kpSiginType;
    private String customeraAuthorizationPic;
    private String customerKpCertificatesPic;
    private String kpName;
    private Integer kpCertificatesType;
    private String kpCertificatesNum;
    private String kpPhoneNum;
    private Integer bankId;
    private String bankNum;
    private Integer status;
    private Integer customerId;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
