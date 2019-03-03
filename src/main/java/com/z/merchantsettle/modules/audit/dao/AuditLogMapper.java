package com.z.merchantsettle.modules.audit.dao;

import com.z.merchantsettle.modules.audit.domain.db.AuditLogDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogMapper {

    List<AuditLogDB> getLogByAuditTaskId(@Param("auditTaskId") Integer auditTaskId);

    void saveAuditLog(AuditLogDB auditLogDB);
}
