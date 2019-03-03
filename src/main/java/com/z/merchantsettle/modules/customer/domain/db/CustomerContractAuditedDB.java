package com.z.merchantsettle.modules.customer.domain.db;

import lombok.Data;

@Data
public class CustomerContractAuditedDB {

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
}
