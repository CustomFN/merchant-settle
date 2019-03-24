package com.z.merchantsettle.modules.poi.service.impl;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.audit.constants.AuditApplicationTypeEnum;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.poi.AuditBusinessInfo;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiBaseInfoDBMapper;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiBaseInfoDB;
import com.z.merchantsettle.modules.poi.service.WmPoiBaseInfoAuditedService;
import com.z.merchantsettle.modules.poi.service.WmPoiBaseInfoService;
import com.z.merchantsettle.modules.poi.service.WmPoiBusinessInfoService;
import com.z.merchantsettle.modules.poi.service.WmPoiOpLogService;
import com.z.merchantsettle.utils.TransferUtil;
import com.z.merchantsettle.utils.transfer.poi.WmPoiTransferUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WmPoiBusinessInfoServiceImpl implements WmPoiBusinessInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiBusinessInfoServiceImpl.class);

    @Autowired
    private WmPoiBaseInfoDBMapper wmPoiBaseInfoDBMapper;

    @Autowired
    private WmPoiBaseInfoService wmPoiBaseInfoService;

    @Autowired
    private ApiAuditService apiAuditService;

    @Autowired
    private WmPoiBaseInfoAuditedService wmPoiBaseInfoAuditedService;

    @Autowired
    private WmPoiOpLogService wmPoiOpLogService;


    @Override
    @Transactional
    public WmPoiBaseInfo saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo, String userId) {
        if (wmPoiBaseInfo == null || StringUtils.isBlank(userId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiBaseInfoDB wmPoiBaseInfoDB = WmPoiTransferUtil.transWmPoiBaseInfo2DB(wmPoiBaseInfo);
        boolean isNew = !(wmPoiBaseInfoDB.getId() != null && wmPoiBaseInfoDB.getId() > 0);
        if (isNew) {
            wmPoiBaseInfoDB.setBusinessInfoStatus(PoiConstant.PoiModuleStatus.AUDING.getCode());
            wmPoiBaseInfoDBMapper.insertSelective(wmPoiBaseInfoDB);
        } else {
            wmPoiBaseInfoDB.setBusinessInfoStatus(PoiConstant.PoiModuleStatus.AUDING.getCode());
            wmPoiBaseInfoDBMapper.updateSelective(wmPoiBaseInfoDB);
        }
        wmPoiBaseInfo = WmPoiTransferUtil.transWmPoiBaseInfoDB2Bo(wmPoiBaseInfoDB);

        wmPoiOpLogService.addLog(wmPoiBaseInfo.getId(), PoiConstant.PoiModuleName.POI_BUSINESS_INFO, "保存门店营业信息", userId);
        commitAudit(wmPoiBaseInfo, isNew, userId);
        wmPoiOpLogService.addLog(wmPoiBaseInfo.getId(), PoiConstant.PoiModuleName.POI_BUSINESS_INFO, "门店营业信息提交审核成功", userId);
        return wmPoiBaseInfo;
    }

    private void commitAudit(WmPoiBaseInfo wmPoiBaseInfo, boolean isNew, String userId) {
        AuditTask auditTask = new AuditTask();
        auditTask.setCustomerId(wmPoiBaseInfo.getCustomerId());
        auditTask.setPoiId(wmPoiBaseInfo.getId());
        auditTask.setAuditApplicationType(isNew ? AuditApplicationTypeEnum.AUDIT_NEW.getCode() : AuditApplicationTypeEnum.AUDIT_UPDATE.getCode());
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditTypeEnum.POI_BUSINESS_INFO.getCode());
        auditTask.setSubmitterId(userId);

        AuditBusinessInfo auditBusinessInfo = new AuditBusinessInfo();
        TransferUtil.transferAll(wmPoiBaseInfo, auditBusinessInfo);
        auditBusinessInfo.setRecordId(wmPoiBaseInfo.getId());
        auditBusinessInfo.setWmPoiId(wmPoiBaseInfo.getId());
        auditBusinessInfo.setOrderMealDate(StringUtils.join(wmPoiBaseInfo.getOrderMealDateList(), "/"));
        auditTask.setAuditData(JSON.toJSONString(auditBusinessInfo));
        apiAuditService.commitAudit(auditTask);
    }

    @Override
    public WmPoiBaseInfo getWmPoiBaseInfoById(Integer wmPoiId, Integer effective) {
        return wmPoiBaseInfoService.getWmPoiBaseInfoById(wmPoiId, effective);
    }

    @Override
    @Transactional
    public void setupEffectWmPoiBusinessInfo(Integer wmPoiId) {
        LOGGER.info("setupEffectWmPoiBusinessInfo wmPoiId = {}", wmPoiId);

        WmPoiBaseInfoDB wmPoiBaseInfoDB = wmPoiBaseInfoDBMapper.getById(wmPoiId);
        if (wmPoiBaseInfoDB == null) {
            return;
        }

        wmPoiBaseInfoDB.setBusinessInfoStatus(PoiConstant.PoiModuleStatus.EFFECT.getCode());
        wmPoiBaseInfoDBMapper.updateSelective(wmPoiBaseInfoDB);

        wmPoiBaseInfoAuditedService.saveOrUpdate(WmPoiTransferUtil.transWmPoiBaseInfoDB2Bo(wmPoiBaseInfoDB));

        wmPoiOpLogService.addLog(wmPoiId, PoiConstant.PoiModuleName.POI_BUSINESS_INFO, "门店营业信息提交审核成功", "系统()");
    }

    @Override
    public void updateByIdForAudit(WmPoiBaseInfo wmPoiBaseInfo, String opUserId) {
        LOGGER.info("updateByIdForAudit wmPoiBaseInfo = {}, opUserId = {}", JSON.toJSONString(wmPoiBaseInfo), opUserId);
        if (wmPoiBaseInfo == null || StringUtils.isBlank(opUserId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiBaseInfoDB wmPoiBaseInfoDB = wmPoiBaseInfoDBMapper.getById(wmPoiBaseInfo.getId());
        if (wmPoiBaseInfoDB == null) {
            throw new PoiException(PoiConstant.POI_OP_ERROR, "更新门店营业信息审核状态异常");
        }

        wmPoiBaseInfoDB.setStatus(wmPoiBaseInfo.getBusinessInfoStatus());
        wmPoiBaseInfoDB.setAuditResult(wmPoiBaseInfo.getBusinessInfoAuditResult());
        wmPoiBaseInfoDBMapper.updateSelective(wmPoiBaseInfoDB);

        String log = "审核结果:审核驳回:" + wmPoiBaseInfoDB.getAuditResult();
        wmPoiOpLogService.addLog(wmPoiBaseInfoDB.getId(), PoiConstant.PoiModuleName.POI_BUSINESS_INFO, log, opUserId);
    }
}
