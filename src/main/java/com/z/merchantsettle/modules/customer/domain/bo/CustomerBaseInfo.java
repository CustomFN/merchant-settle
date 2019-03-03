package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class CustomerBaseInfo {

    private Integer customerId;
    private String customerName;
    private String customerType;
    private Integer customerPoiRelNum;
    private String customerPrincipal;
    private String customerStatus;
}
