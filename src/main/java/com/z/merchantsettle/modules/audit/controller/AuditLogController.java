package com.z.merchantsettle.modules.audit.controller;

import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.domain.bo.AuditLog;
import com.z.merchantsettle.modules.audit.service.AuditLogService;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.exception.AuditException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit/log")
public class AuditLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogController.class);

    @Autowired
    private AuditLogService auditLogService;

    @RequestMapping("/{auditTaskId}")
    public Object getLogByAuditTaskId(@PathVariable(name = "auditTaskId") Integer auditTaskId) {
        if (auditTaskId == null || auditTaskId <= 0) {
            return ReturnResult.fail(AuditConstant.AUDIT_PARAM_ERROR, "参数错误");
        }

        try {
            List<AuditLog> auditLogList = auditLogService.getLogByAuditTaskId(auditTaskId);
            return ReturnResult.success(auditLogList);
        } catch (AuditException e) {
            LOGGER.error("获取审核日志失败 auditTaskId = {}", auditTaskId);
            return ReturnResult.fail(e.getMsg());
        }
    }
}
