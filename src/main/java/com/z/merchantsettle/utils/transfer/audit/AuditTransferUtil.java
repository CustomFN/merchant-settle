package com.z.merchantsettle.utils.transfer.audit;

import com.google.common.collect.Lists;
import com.z.merchantsettle.modules.audit.domain.bo.AuditLog;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.db.AuditLogDB;
import com.z.merchantsettle.modules.audit.domain.db.AuditTaskDB;
import com.z.merchantsettle.utils.TransferUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class AuditTransferUtil {

    public static AuditTaskDB transAuditTaskBo2DB(AuditTask auditTask) {
        if (auditTask == null) {
            return null;
        }

        AuditTaskDB auditTaskDB = new AuditTaskDB();
        TransferUtil.transferAll(auditTask, auditTaskDB);
        return auditTaskDB;
    }

    public static List<AuditTaskDB> transAuditTaskList2DBList(List<AuditTask> auditTaskList) {
        if (CollectionUtils.isEmpty(auditTaskList)) {
            return Lists.newArrayList();
        }

        List<AuditTaskDB> auditTaskDBList = Lists.newArrayList();
        for (AuditTask auditTask : auditTaskList) {
            auditTaskDBList.add(transAuditTaskBo2DB(auditTask));
        }
        return auditTaskDBList;
    }

    public static AuditTask transAuditTaskDB2Bo(AuditTaskDB auditTaskDB) {
        if (auditTaskDB == null) {
            return null;
        }

        AuditTask auditTask = new AuditTask();
        TransferUtil.transferAll(auditTaskDB, auditTask);
        return auditTask;
    }

    public static List<AuditTask> transAuditTaskDBList2BoList(List<AuditTaskDB> auditTaskDBList) {
        if (CollectionUtils.isEmpty(auditTaskDBList)) {
            return Lists.newArrayList();
        }

        List<AuditTask> auditTaskList = Lists.newArrayList();
        for (AuditTaskDB auditTaskDB : auditTaskDBList) {
            auditTaskList.add(transAuditTaskDB2Bo(auditTaskDB));
        }
        return auditTaskList;
    }

    public static AuditLog transAuditLogDB2Bo(AuditLogDB auditLogDB) {
        if (auditLogDB == null) {
            return null;
        }

        AuditLog auditLog = new AuditLog();
        TransferUtil.transferAll(auditLogDB, auditLog);
        return auditLog;
    }

    public static List<AuditLog> transAuditLogDBList2BoList(List<AuditLogDB> auditLogDBList) {
        if (CollectionUtils.isEmpty(auditLogDBList)) {
            return Lists.newArrayList();
        }

        List<AuditLog> auditLogList = Lists.newArrayList();
        for (AuditLogDB auditLogDB : auditLogDBList) {
            auditLogList.add(transAuditLogDB2Bo(auditLogDB));
        }
        return auditLogList;
    }

    public static AuditLogDB transAuditLog2DB(AuditLog auditLog) {
        if (auditLog == null) {
            return null;
        }

        AuditLogDB auditLogDB = new AuditLogDB();
        TransferUtil.transferAll(auditLog, auditLogDB);
        return auditLogDB;
    }

    public static List<AuditLogDB> transAuditLogList2DBList(List<AuditLog> auditLogList) {
        if (CollectionUtils.isEmpty(auditLogList)) {
            return Lists.newArrayList();
        }

        List<AuditLogDB> auditLogDBList = Lists.newArrayList();
        for (AuditLog auditLog :auditLogList) {
            auditLogDBList.add(transAuditLog2DB(auditLog));
        }
        return auditLogDBList;
    }
}
