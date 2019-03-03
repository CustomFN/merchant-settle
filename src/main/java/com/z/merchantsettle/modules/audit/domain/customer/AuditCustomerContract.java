package com.z.merchantsettle.modules.audit.domain.customer;

import lombok.Data;

@Data
public class AuditCustomerContract {

    private Integer contractId;

    private Integer customerId;
    private Integer customerContractType;
    private String customerContractNum;
    private Long contractEndTime;
    private String contractScan;

    private String partyA;
    private String partyAContactPerson;
    private String partyAContactPersonPhone;
    private Long partyASignTime;

    private String partyB;
    private String partyBContactPerson;
    private String partyBContactPersonPhone;
    private Long partyBSignTime;
}
