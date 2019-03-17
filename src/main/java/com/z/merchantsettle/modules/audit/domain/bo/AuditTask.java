package com.z.merchantsettle.modules.audit.domain.bo;

import lombok.Data;

@Data
public class AuditTask {

    private Integer id;
    private Integer customerId;
    private Integer poiId;
    private Integer auditType;
    private String auditTypeStr;
    private Integer auditApplicationType;
    private String auditApplicationTypeStr;
    private String auditData;
    private Object auditDataObj;
    private Integer auditStatus;
    private String submitterId;
    private String transactor = "";
    private String auditResult;
    private Long ctime;
    private Long utime;
    private Integer valid;
    private Integer completed;

}
