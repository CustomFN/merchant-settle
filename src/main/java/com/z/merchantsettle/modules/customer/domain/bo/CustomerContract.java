package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CustomerContract {

    private Integer id;
    private Integer customerContractType;

    @NotBlank(message = "合同编号不能为空")
    private String customerContractNum;

    @NotNull(message = "合同有效期不能为空")
    private Long contractEndTime;
    private String contractScan;

    @NotEmpty(message = "合同扫描件不能为空")
    private List<String> contractScanList;
    private Integer status;
    private String statusStr;
    private Long ctime;
    private Long utime;
    private Integer valid;
    private Integer customerId;
    private String auditResult;
    private CustomerSigner partyA;
    private CustomerSigner partyB;
}
