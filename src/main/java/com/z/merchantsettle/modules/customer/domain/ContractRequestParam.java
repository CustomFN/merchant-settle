package com.z.merchantsettle.modules.customer.domain;

import lombok.Data;

@Data
public class ContractRequestParam {

    private Integer contractId;
    private Integer customerId;
    private String contractNum;

}
