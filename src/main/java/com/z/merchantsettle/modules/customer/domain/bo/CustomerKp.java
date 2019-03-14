package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class CustomerKp {

    private Integer id;
    private Integer kpType;
    private Integer kpSiginType;
    private String kpAuthorizationPic;
    private List<String> kpAuthorizationPicList;
    private String kpCertificatesPic;
    private List<String> kpCertificatesPicList;
    private String kpName;
    private Integer kpCertificatesType;
    private String kpCertificatesNum;
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