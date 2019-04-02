package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CustomerSigner {

    private Integer id;
    private Integer contractId;
    private String signerLabel;

    @NotBlank(message = "合同签约方不能为空")
    private String party;

    @NotBlank(message = "合同签约方签约人不能为空")
    private String partyContactPerson;

    @NotBlank(message = "合同签约方签约人手机号不能为空")
    private String partyContactPersonPhone;

    @NotNull(message = "合同签约方签约时间不能为空")
    private Long signTime;
    private Long ctime;
    private Long utime;
    private Integer valid;

}
