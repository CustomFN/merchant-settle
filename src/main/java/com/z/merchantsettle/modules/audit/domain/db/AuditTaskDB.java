package com.z.merchantsettle.modules.audit.domain.db;

import lombok.Data;

@Data
public class AuditTaskDB {

    private Integer id;
    private Integer customerId;
    private Integer poiId;
    private Integer auditType;
    private Integer auditApplicationType;
    private String auditData;
    private Integer auditStatus;
    private String submitterId;
    private String transactor;
    private String auditResult;
    private Long ctime;
    private Long utime;
    private Integer valid;
    private Integer completed;

}
