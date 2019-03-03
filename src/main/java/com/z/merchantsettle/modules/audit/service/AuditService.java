package com.z.merchantsettle.modules.audit.service;

import com.z.merchantsettle.modules.audit.domain.AuditResult;
import com.z.merchantsettle.modules.audit.domain.AuditSearchParam;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.AuditException;

public interface AuditService {

    PageData<AuditTask> getAuditTaskList(AuditSearchParam auditSearchParam, Integer pageNum, Integer pageSize);

    AuditTask getAuditTaskDetailById(Integer auditTaskId) throws AuditException;

    void assignTaskTransactor(Integer auditTaskId, String transactor, String opUserId) throws AuditException;

    void saveAuditResult(AuditResult result);
}
