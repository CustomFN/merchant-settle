package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class ContractBaseInfo {

    private Integer contractId;
    private String contractNum;
    private String partyAName;
    private String partyASignerName;
    private String partyBSignerName;
    private String principal;
    private String auditStatus;
    private Long signerTime;
    private Long contractValidTime;
    private String contractType;
}
