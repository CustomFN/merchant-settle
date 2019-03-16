package com.z.merchantsettle.mq.customer;

import lombok.Data;

@Data
public class CustomerMsg {

    private Integer customerId;
    private Integer type;
    private String msg;
}
