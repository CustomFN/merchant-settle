package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class CustomerContractAudited {

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
    private CustomerSignerAudited partyA;
    private CustomerSignerAudited partyB;
}
