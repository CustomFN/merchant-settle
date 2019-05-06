package com.z.merchantsettle.modules.audit.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.dao.AuditMapper;
import com.z.merchantsettle.modules.audit.domain.AuditResult;
import com.z.merchantsettle.modules.audit.domain.AuditSearchParam;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerContract;
import com.z.merchantsettle.modules.audit.domain.customer.AuditCustomerSettle;
import com.z.merchantsettle.modules.audit.domain.db.AuditLogDB;
import com.z.merchantsettle.modules.audit.domain.db.AuditTaskDB;
import com.z.merchantsettle.modules.audit.domain.poi.AuditWmPoiBaseInfo;
import com.z.merchantsettle.modules.audit.domain.poi.AuditWmPoiDeliveryInfo;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.audit.service.AuditLogService;
import com.z.merchantsettle.modules.audit.service.AuditService;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.AuditException;
import com.z.merchantsettle.modules.audit.service.callback.AuditCallbackService;
import com.z.merchantsettle.modules.customer.service.callback.CustomerCallbackService;
import com.z.merchantsettle.modules.poi.service.callback.WmPoiCallBackService;
import com.z.merchantsettle.utils.transfer.audit.AuditTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService, ApiAuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditServiceImpl.class);

    @Autowired
    private AuditMapper auditMapper;
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private AuditCallbackService auditCallbackService;


    /****   ApiService   ****/

    @Override
    public void commitAudit(AuditTask auditTask) throws AuditException {
        LOGGER.info("audit auditTask = {}", JSON.toJSONString(auditTask));
        if (auditTask == null) {
            throw new AuditException(AuditConstant.AUDIT_PARAM_ERROR, "提审参数错误");
        }

        AuditTaskDB auditTaskDB = AuditTransferUtil.transAuditTaskBo2DB(auditTask);
        auditMapper.saveAuditTask(auditTaskDB);

        AuditLogDB auditLogDB = new AuditLogDB();
        auditLogDB.setAuditTaskId(auditTaskDB.getId());
        auditLogDB.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditLogDB.setOpUserId(auditTask.getSubmitterId());
        auditLogDB.setLogMsg("提交审核");
        auditLogService.saveAuditLog(auditLogDB);
    }



    /****   AuditService   ****/

    @Override
    public PageData<AuditTask> getAuditTaskList(AuditSearchParam auditSearchParam, Integer pageNum, Integer pageSize) {
        LOGGER.info("getAuditTaskList auditSearchParam = {}", JSON.toJSONString(auditSearchParam));
        PageHelper.startPage(pageNum, pageSize);
        List<AuditTaskDB> auditTaskDBList;
        // 这里会判断审核任务是否分配，通过判断transactor是否等于""
//        if (StringUtils.isNotBlank(auditSearchParam.getTransactor())) {
//            // 用于个人任务子模块的列表展示
//            auditTaskDBList = auditMapper.selectListByTransactor(auditSearchParam);
//        } else {
//            // 用于所有任务子模块的列表展示
            auditTaskDBList = auditMapper.selectList(auditSearchParam);
//        }

        PageInfo<AuditTaskDB> pageInfo = new PageInfo<>(auditTaskDBList);
        List<AuditTask> auditTaskList = AuditTransferUtil.transAuditTaskDBList2BoList(auditTaskDBList);
        for (AuditTask task : auditTaskList) {
            task.setAuditData("");
        }
        return new PageData.Builder<AuditTask>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPages())
                .data(auditTaskList)
                .build();
    }

    @Override
    public AuditTask getAuditTaskDetailById(Integer auditTaskId) throws AuditException {
        if (auditTaskId == null || auditTaskId <= 0) {
            throw new AuditException(AuditConstant.AUDIT_PARAM_ERROR, "参数错误");
        }
        AuditTaskDB auditTaskDB = auditMapper.getAuditTaskDetailById(auditTaskId);
        return AuditTransferUtil.transAuditTaskDB2Bo(auditTaskDB);
    }

    @Override
    public void assignTaskTransactor(Integer auditTaskId, String transactor, String opUserId) throws AuditException {
        if (auditTaskId == null || auditTaskId < 0 || StringUtils.isBlank(transactor)) {
            throw new AuditException(AuditConstant.AUDIT_PARAM_ERROR, "参数错误");
        }

        AuditTaskDB auditTaskDB = new AuditTaskDB();
        auditTaskDB.setId(auditTaskId);
        auditTaskDB.setTransactor(transactor);
        auditMapper.updateByTaskIdSelective(auditTaskDB);

        AuditLogDB auditLogDB = new AuditLogDB();
        auditLogDB.setAuditTaskId(auditTaskDB.getId());
        auditLogDB.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditLogDB.setOpUserId(opUserId);
        auditLogDB.setLogMsg(String.format("审核任务处理人变更为: %s", transactor));
        auditLogService.saveAuditLog(auditLogDB);
    }

    @Override
    @Transactional
    public void saveAuditResult(AuditResult result) throws AuditException {
        if (result == null || result.getAuditStatus() == null || result.getAuditStatus() <= 0) {
            throw new AuditException(AuditConstant.AUDIT_STATUS_ERROR, "审核状态异常");
        }


        AuditTaskDB auditTaskDB = auditMapper.getAuditTaskDetailById(result.getAuditTaskId());
        if (auditTaskDB == null) {
            throw new AuditException(AuditConstant.AUDIT_OP_ERROR, "审核任务状态错误");
        }

        // 更新审核任务
        auditTaskDB.setId(result.getAuditTaskId());
        auditTaskDB.setAuditStatus(result.getAuditStatus());
        auditTaskDB.setAuditResult(result.getResult());
        auditTaskDB.setCompleted(1);
        auditMapper.updateByTaskIdSelective(auditTaskDB);
        // 记录审核操作日志
        AuditLogDB auditLogDB = new AuditLogDB();
        auditLogDB.setAuditTaskId(auditTaskDB.getId());
        auditLogDB.setAuditStatus(result.getAuditStatus());
        auditLogDB.setOpUserId(result.getOpUser());

        String logContent = AuditConstant.AuditStatus.AUDIT_PASS == result.getAuditStatus()
                ? "审核通过" : "审核驳回\n" + result.getResult();
        auditLogDB.setLogMsg(logContent);
        LOGGER.info("saveAuditResult auditLogDB = {}", JSON.toJSONString(auditLogDB));
        auditLogService.saveAuditLog(auditLogDB);

        AuditTask auditTask = AuditTransferUtil.transAuditTaskDB2Bo(auditTaskDB);
        // 根据审核类型获取不同模块的审核回调处理对象对审核结果进行处理
        auditCallbackService.getCallbackHandler(auditTask.getAuditType()).handleCallback(result, auditTask);
    }

}
