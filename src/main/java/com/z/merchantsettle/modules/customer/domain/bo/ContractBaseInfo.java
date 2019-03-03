package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class ContractBaseInfo {

    private Integer contractId;
    private String contractNum;
    private String partyAName;
    private String partyASignerName;
    private String partyBName;
    private String principal;
    private String auditStatus;
    private String signerTime;
    private String contractValidTime;
    private String contractType;
}
