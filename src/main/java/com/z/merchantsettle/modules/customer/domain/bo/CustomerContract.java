package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class CustomerContract {

    private Integer id;
    private Integer customerContractType;
    private String customerContractNum;
    private Long contractEndTime;
    private String contractScan;
    private Integer status;
    private Long ctime;
    private Long utime;
    private Integer valid;
    private Integer customerId;
    private String auditResult;
    private CustomerSigner partyA;
    private CustomerSigner partyB;
}
