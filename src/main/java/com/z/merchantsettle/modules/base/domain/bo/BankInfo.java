package com.z.merchantsettle.modules.base.domain.bo;

import lombok.Data;

@Data
public class BankInfo {

    private Integer bankId;
    private String bankName;
    private String subBankId;
    private String subBankName;

}
