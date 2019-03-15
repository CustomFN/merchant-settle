package com.z.merchantsettle.modules.customer.domain;

import lombok.Data;

@Data
public class CustomerOpLog {

    private Integer id;
    private Integer customerId;
    private String module;
    private String content;
    private String opUser;
    private String opUserId;
    private Long ctime;

}
