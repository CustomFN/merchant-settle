package com.z.merchantsettle.modules.audit.service;

import com.z.merchantsettle.modules.audit.domain.bo.AuditLog;
import com.z.merchantsettle.modules.audit.domain.db.AuditLogDB;
import com.z.merchantsettle.exception.AuditException;

import java.util.List;

public interface AuditLogService {

    List<AuditLog> getLogByAuditTaskId(Integer auditTaskId) throws AuditException;

    void saveAuditLog(AuditLogDB auditLogDB) throws AuditException;
}
