package com.z.merchantsettle.modules.audit.domain.customer;

import lombok.Data;

@Data
public class AuditCustomer {

    private Integer customerId;
    private String customerTypeStr;
    private String[] customerCertificatesPicArr;
    private String customerCertificatesNum;
    private String customerName;
    private String customerLegalPerson;
    private String customerCertificatesAddress;
    private String customerValidTime;

}
