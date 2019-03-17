package com.z.merchantsettle.modules.audit.domain;

import lombok.Data;

import java.util.List;

@Data
public class AuditSearchParam {

    private Integer customerId;
    private Integer poiId;
    private String submitterId;
    private String transactor = "";
    private Integer completed;
}
