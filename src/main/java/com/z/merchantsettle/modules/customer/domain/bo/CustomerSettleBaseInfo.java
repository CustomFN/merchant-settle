package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class CustomerSettleBaseInfo {

    private Integer id;
    private String financialOfficer;
    private String settleAccName;
    private String settleAccNo;
    private Integer settlePoiRelNum;
}
