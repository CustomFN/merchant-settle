package com.z.merchantsettle.modules.audit.domain.bo;

import lombok.Data;

@Data
public class AuditLog {

    private Integer id;
    private Integer auditTaskId;
    private Integer customerId;
    private Integer poiId;
    private Integer auditStatus;
    private String logMsg;
    private String submitterId;
    private Long ctime;
}
