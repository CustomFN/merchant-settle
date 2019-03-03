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
import com.z.merchantsettle.modules.customer.service.callback.CustomerCallbackService;
import com.z.merchantsettle.modules.poi.service.callback.WmPoiCallBackService;
import com.z.merchantsettle.utils.transfer.audit.AuditTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService, ApiAuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditServiceImpl.class);

    @Autowired
    private AuditMapper auditMapper;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private CustomerCallbackService customerCallbackService;

    @Autowired
    private WmPoiCallBackService wmPoiCallBackService;


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
        PageHelper.startPage(pageNum, pageSize);
        List<AuditTaskDB> auditTaskDBList = auditMapper.selectList(auditSearchParam);

        List<AuditTask> auditTaskList = AuditTransferUtil.transAuditTaskDBList2BoList(auditTaskDBList);
        PageInfo<AuditTask> pageInfo = new PageInfo<>(auditTaskList);
        return new PageData.Builder<AuditTask>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPageSize())
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
    public void saveAuditResult(AuditResult result) throws AuditException {
        if (result == null || result.getAuditStatus() == null || result.getAuditStatus() <= 0) {
            throw new AuditException(AuditConstant.AUDIT_STATUS_ERROR, "审核状态异常");
        }


        AuditTaskDB auditTaskDB = auditMapper.getAuditTaskDetailById(result.getAuditTaskId());
        if (auditTaskDB == null) {
            throw new AuditException(AuditConstant.AUDIT_OP_ERROR, "审核任务状态错误");
        }

        auditTaskDB.setId(result.getAuditTaskId());
        auditTaskDB.setAuditStatus(result.getAuditStatus());
        auditTaskDB.setAuditResult(result.getResult());
        auditMapper.updateByTaskIdSelective(auditTaskDB);

        AuditLogDB auditLogDB = new AuditLogDB();
        auditLogDB.setAuditTaskId(auditTaskDB.getId());
        auditLogDB.setAuditStatus(result.getAuditStatus());
        auditLogDB.setOpUserId(result.getOpUser());

        String logContent = AuditConstant.AuditStatus.AUDIT_PASS == result.getAuditStatus()
                ? "审核通过" : "审核驳回\n" + result.getResult();
        auditLogDB.setLogMsg(logContent);
        auditLogService.saveAuditLog(auditLogDB);

        callbackModules(result, auditTaskDB);
    }

    private void callbackModules(AuditResult result, AuditTaskDB auditTaskDB) {
        Integer customerId = auditTaskDB.getCustomerId();
        Integer wmPoiId = auditTaskDB.getPoiId();
        Integer auditStatus = result.getAuditStatus();
        String auditResult = result.getResult();
        String opUser = result.getOpUser();

        switch (auditTaskDB.getAuditType()) {
            case AuditConstant.AuditType.CUSTOMER :
                customerCallbackService.customerAuditCallback(customerId, auditStatus, auditResult);
                break;
            case AuditConstant.AuditType.CUSTOMER_KP :
                customerCallbackService.customerKpAuditCallback(customerId, auditStatus, auditResult);
                break;
            case AuditConstant.AuditType.CUSTOMER_CONTRACT :
                AuditCustomerContract auditCustomerContract = JSON.parseObject(auditTaskDB.getAuditData(), AuditCustomerContract.class);
                Integer contractId = auditCustomerContract.getContractId();

                customerCallbackService.customerContractAuditCallback(customerId, contractId, auditStatus, auditResult);
                break;
            case AuditConstant.AuditType.CUSTOMER_SETTLE :
                AuditCustomerSettle auditCustomerSettle = JSON.parseObject(auditTaskDB.getAuditData(), AuditCustomerSettle.class);
                Integer settleId = auditCustomerSettle.getSettleId();

                customerCallbackService.customerSettleAuditCallback(customerId, settleId, auditStatus, auditResult);
                break;

            case AuditConstant.AuditType.POI_BASE_INFO :
                wmPoiCallBackService.WmPoiBaseInfoAuditCallBack(wmPoiId, auditStatus, auditResult, opUser);
                break;
            case AuditConstant.AuditType.POI_QUA_INFO :
                AuditWmPoiBaseInfo auditWmPoiBaseInfo = JSON.parseObject(auditTaskDB.getAuditData(), AuditWmPoiBaseInfo.class);

                wmPoiCallBackService.WmPoiQuaAuditCallBack(auditWmPoiBaseInfo.getRecordId(), wmPoiId, auditStatus, auditResult, opUser);
                break;
            case AuditConstant.AuditType.POI_DELIVERY_INFO :
                AuditWmPoiDeliveryInfo auditWmPoiDeliveryInfo = JSON.parseObject(auditTaskDB.getAuditData(), AuditWmPoiDeliveryInfo.class);

                wmPoiCallBackService.WmPoiDeliveryInfoAuditCallBack(auditWmPoiDeliveryInfo.getRecordId(), wmPoiId, auditStatus, auditResult, opUser);
                break;
            case AuditConstant.AuditType.POI_BUSINESS_INFO :
                wmPoiCallBackService.WmPoiBusinessInfoAuditCallBack(wmPoiId, auditStatus, auditResult, opUser);
                break;
        }
    }
}
