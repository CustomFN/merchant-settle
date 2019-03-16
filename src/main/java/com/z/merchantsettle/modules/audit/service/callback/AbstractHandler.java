package com.z.merchantsettle.modules.audit.service.callback;

import com.z.merchantsettle.modules.audit.domain.AuditResult;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;

public abstract class AbstractHandler {

    public abstract void handleCallback(AuditResult result, AuditTask auditTask);

}
