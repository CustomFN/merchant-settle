package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

import java.util.List;

@Data
public class CustomerAudited {

    private Integer id;
    private Integer customerType;
    private String customerCertificatesPic;
    private List<String> customerCertificatesPicList;
    private String customerCertificatesNum;
    private String customerName;
    private String customerLegalPerson;
    private String customerCertificatesAddress;
    private Integer status;
    private String statusStr;
    private Long customerValidTime;
    private String customerPrincipal;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
