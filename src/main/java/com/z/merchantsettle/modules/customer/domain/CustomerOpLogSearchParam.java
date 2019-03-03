package com.z.merchantsettle.modules.customer.domain;

import lombok.Data;

@Data
public class CustomerOpLogSearchParam {

    private Integer customerId;
    private String module;
    private String content;
    private String opUser;
}
