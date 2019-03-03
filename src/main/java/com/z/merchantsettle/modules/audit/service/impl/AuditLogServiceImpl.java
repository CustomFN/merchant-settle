package com.z.merchantsettle.modules.audit.service.impl;

import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.dao.AuditLogMapper;
import com.z.merchantsettle.modules.audit.domain.bo.AuditLog;
import com.z.merchantsettle.modules.audit.domain.db.AuditLogDB;
import com.z.merchantsettle.modules.audit.service.AuditLogService;
import com.z.merchantsettle.exception.AuditException;
import com.z.merchantsettle.utils.transfer.audit.AuditTransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Override
    public List<AuditLog> getLogByAuditTaskId(Integer auditTaskId) throws AuditException {
        if (auditTaskId == null || auditTaskId <= 0) {
            throw new AuditException(AuditConstant.AUDIT_PARAM_ERROR, "参数错误");
        }

        List<AuditLogDB> auditLogDBList = auditLogMapper.getLogByAuditTaskId(auditTaskId);
        return AuditTransferUtil.transAuditLogDBList2BoList(auditLogDBList);
    }

    @Override
    public void saveAuditLog(AuditLogDB auditLogDB) throws AuditException {
        try {
            auditLogMapper.saveAuditLog(auditLogDB);
        } catch (Exception e) {
            throw new AuditException(AuditConstant.AUDIT_OP_ERROR, "保存审核日志异常");
        }
    }
}
