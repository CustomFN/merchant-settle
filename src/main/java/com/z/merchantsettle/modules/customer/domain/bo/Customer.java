package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class Customer {

    private Integer id;
    private Integer customerType;
    private String customerCertificatesPic;
    private String customerCertificatesNum;
    private String customerName;
    private String customerLegalPerson;
    private String customerCertificatesAddress;
    private Integer status;
    private Long customerValidTime;
    private String customerPrincipal;
    private String auditResult;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
