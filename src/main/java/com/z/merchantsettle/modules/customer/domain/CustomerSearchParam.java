package com.z.merchantsettle.modules.customer.domain;

import lombok.Data;

@Data
public class CustomerSearchParam {

    private Integer customerId;
    private String customerName;
    private String customerPrincipal;
}
