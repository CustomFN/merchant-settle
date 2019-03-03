package com.z.merchantsettle.modules.audit.service;

import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.exception.AuditException;

public interface ApiAuditService {

    void commitAudit(AuditTask auditTask) throws AuditException;
}
