package com.z.merchantsettle.modules.audit.domain.db;

import lombok.Data;

@Data
public class AuditLogDB {

    private Integer id;
    private Integer auditTaskId;
    private Integer auditStatus;
    private String logMsg;
    private String opUserId;
    private Long opTime;
}
