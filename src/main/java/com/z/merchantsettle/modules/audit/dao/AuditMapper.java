package com.z.merchantsettle.modules.audit.dao;

import com.z.merchantsettle.modules.audit.domain.AuditSearchParam;
import com.z.merchantsettle.modules.audit.domain.db.AuditTaskDB;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditMapper {

    void saveAuditTask(AuditTaskDB auditTaskDB);

    List<AuditTaskDB> selectList(@Param("auditSearchParam") AuditSearchParam auditSearchParam);

    AuditTaskDB getAuditTaskDetailById(Integer auditTaskId);

    void updateByTaskIdSelective(AuditTaskDB auditTaskDB);
}
