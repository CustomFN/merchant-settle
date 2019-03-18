package com.z.merchantsettle.modules.audit.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.domain.AuditResult;
import com.z.merchantsettle.modules.audit.domain.AuditSearchParam;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.service.AuditService;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.common.ReturnResult;
import com.z.merchantsettle.exception.AuditException;
import com.z.merchantsettle.modules.upm.domain.bo.User;
import com.z.merchantsettle.utils.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditController.class);

    @Autowired
    private AuditService auditService;

    @RequestMapping("/list")
    public Object listAuditTask(AuditSearchParam auditSearchParam,
                                @RequestParam(defaultValue = "1", name = "pageNum") Integer pageNum,
                                @RequestParam(defaultValue = "30", name = "pageSize") Integer pageSize) {

        LOGGER.info("listAuditTask auditSearchParam = {}, pageNum = {}, pageSize = {}", JSON.toJSONString(auditSearchParam), pageNum, pageSize);
        PageData<AuditTask> pageData = auditService.getAuditTaskList(auditSearchParam, pageNum, pageSize);
        return ReturnResult.success(pageData);
    }

    @RequestMapping("/detail/{auditTaskId}")
    public Object getAuditJobDetail(@PathVariable(name = "auditTaskId") Integer auditTaskId) {
        if (auditTaskId == null || auditTaskId <= 0) {
            return ReturnResult.fail(AuditConstant.AUDIT_PARAM_ERROR, "参数错误");
        }

        try {
            AuditTask auditTask = auditService.getAuditTaskDetailById(auditTaskId);
            Object obj = JSON.parse(auditTask.getAuditData());
            auditTask.setAuditDataObj(obj);
            return ReturnResult.success(Lists.newArrayList(auditTask));
        } catch (AuditException e) {
            LOGGER.error("查询审核信息异常 auditTaskId = {}", auditTaskId);
            return ReturnResult.fail(e.getMsg());
        }
    }

    @RequestMapping("/assignTaskTransactor")
    public Object assignTaskTransactor(@RequestParam(name = "auditTaskId") Integer auditTaskId, @RequestParam(name = "transactor") String transactor) {
        if (auditTaskId == null || auditTaskId < 0 || StringUtils.isBlank(transactor)) {
            return ReturnResult.fail(AuditConstant.AUDIT_PARAM_ERROR, "参数错误");
        }

        User user = ShiroUtils.getSysUser();
        if (user == null) {
            return ReturnResult.fail(AuditConstant.AUDIT_OP_ERROR, "系统异常");
        }
        auditService.assignTaskTransactor(auditTaskId, transactor, user.getUserId());
        return ReturnResult.success();
    }

    @RequestMapping("/saveAuditResult")
    public Object saveAuditResult( AuditResult result) {
        try {
            Integer auditStatus = StringUtils.isBlank(result.getResult()) ? AuditConstant.AuditStatus.AUDIT_PASS : AuditConstant.AuditStatus.AUDIT_REJECT;
            result.setAuditStatus(auditStatus);
            auditService.saveAuditResult(result);
            return ReturnResult.success();
        } catch (AuditException e) {
            LOGGER.error("保存审核结果异常", e);
            return ReturnResult.fail(e.getCode(), e.getMsg());
        }
    }
}
