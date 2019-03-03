package com.z.merchantsettle.modules.audit.domain;

import lombok.Data;


@Data
public class AuditResult {

    private Integer auditTaskId;
    private Integer auditStatus;
    private String  result;
    private String opUser;
}
