package com.z.merchantsettle.modules.poi.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.audit.constants.AuditApplicationTypeEnum;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.poi.AuditWmPoiDeliveryInfo;
import com.z.merchantsettle.modules.audit.domain.poi.AuditWmPoiProject;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiDeliveryInfoDBMapper;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfo;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiProject;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiDeliveryInfoDB;
import com.z.merchantsettle.modules.poi.service.WmPoiDeliveryInfoAuditedService;
import com.z.merchantsettle.modules.poi.service.WmPoiDeliveryInfoService;
import com.z.merchantsettle.modules.poi.service.WmPoiOpLogService;
import com.z.merchantsettle.utils.TransferUtil;
import com.z.merchantsettle.utils.transfer.poi.WmPoiTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WmPoiDeliveryInfoServiceImpl implements WmPoiDeliveryInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiDeliveryInfoServiceImpl.class);

    @Autowired
    private WmPoiDeliveryInfoDBMapper wmPoiDeliveryInfoDBMapper;

    @Autowired
    private ApiAuditService apiAuditService;

    @Autowired
    private WmPoiDeliveryInfoAuditedService wmPoiDeliveryInfoAuditedService;

    @Autowired
    private WmPoiOpLogService wmPoiOpLogService;


    @Override
    @Transactional
    public WmPoiDeliveryInfo saveOrUpdate(WmPoiDeliveryInfo wmPoiDeliveryInfo, String opUserId) {
        if (wmPoiDeliveryInfo == null || StringUtils.isBlank(opUserId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = WmPoiTransferUtil.transWmPoiDeliveryInfo2DB(wmPoiDeliveryInfo);
        boolean isNew = !(wmPoiDeliveryInfoDB.getId() != null && wmPoiDeliveryInfoDB.getId() > 0);
        if (isNew) {
            wmPoiDeliveryInfoDB.setStatus(PoiConstant.PoiModuleStatus.AUDING.getCode());
            wmPoiDeliveryInfoDBMapper.insertSelective(wmPoiDeliveryInfoDB);
        } else {
            wmPoiDeliveryInfoDBMapper.updateSelective(wmPoiDeliveryInfoDB);
        }
        wmPoiDeliveryInfo = WmPoiTransferUtil.transWmPoiDeliveryInfoDB2Bo(wmPoiDeliveryInfoDB);
        wmPoiOpLogService.addLog(wmPoiDeliveryInfo.getWmPoiId(), PoiConstant.PoiModuleName.POI_DELIVERY_INFO, "保存门店配送信息", opUserId);

        commitAudit(wmPoiDeliveryInfo, isNew, opUserId);
        wmPoiOpLogService.addLog(wmPoiDeliveryInfo.getWmPoiId(), PoiConstant.PoiModuleName.POI_DELIVERY_INFO, "门店配送信息提交审核成功", opUserId);
        return wmPoiDeliveryInfo;
    }

    private void commitAudit(WmPoiDeliveryInfo wmPoiDeliveryInfo, boolean isNew, String opUserId) {
        AuditTask auditTask = new AuditTask();
        auditTask.setPoiId(wmPoiDeliveryInfo.getWmPoiId());
        auditTask.setAuditApplicationType(isNew ? AuditApplicationTypeEnum.AUDIT_NEW.getCode() : AuditApplicationTypeEnum.AUDIT_UPDATE.getCode());
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditTypeEnum.POI_DELIVERY_INFO.getCode());
        auditTask.setSubmitterId(opUserId);

        AuditWmPoiDeliveryInfo auditWmPoiDeliveryInfo = new AuditWmPoiDeliveryInfo();
        TransferUtil.transferAll(wmPoiDeliveryInfo, auditWmPoiDeliveryInfo);

        List<WmPoiProject> wmPoiProjectList = wmPoiDeliveryInfo.getWmPoiProjectList();
        List<AuditWmPoiProject> auditWmPoiProjectList = Lists.newArrayList();
        for (WmPoiProject wmPoiProject : wmPoiProjectList) {
            AuditWmPoiProject auditWmPoiProject = new AuditWmPoiProject();
            TransferUtil.transferAll(wmPoiProject, auditWmPoiProject);
            auditWmPoiProjectList.add(auditWmPoiProject);
        }
        auditWmPoiDeliveryInfo.setWmPoiProjectList(auditWmPoiProjectList);
        auditWmPoiDeliveryInfo.setRecordId(wmPoiDeliveryInfo.getId());
        auditWmPoiDeliveryInfo.setWmPoiId(wmPoiDeliveryInfo.getWmPoiId());
        auditTask.setAuditData(JSON.toJSONString(auditWmPoiDeliveryInfo));
        apiAuditService.commitAudit(auditTask);
    }


    @Override
    public WmPoiDeliveryInfo getWmPoiDeliveryInfoById(Integer wmPoiId, Integer effective) {
        if (wmPoiId == null || wmPoiId <= 0 || effective < 0) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiDeliveryInfo wmPoiDeliveryInfo;
        if (PoiConstant.EFFECTIVE == effective) {
            wmPoiDeliveryInfo  = wmPoiDeliveryInfoAuditedService.getWmPoiDeliveryInfoAuditedById(wmPoiId);
        } else {
            WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = wmPoiDeliveryInfoDBMapper.getByWmPoiId(wmPoiId);
            wmPoiDeliveryInfo = WmPoiTransferUtil.transWmPoiDeliveryInfoDB2Bo(wmPoiDeliveryInfoDB);
        }
        return wmPoiDeliveryInfo;
    }

    @Override
    @Transactional
    public void setupEffectWmPoiDeliveryInfo(Integer wmPoiId) {
        LOGGER.info("setupEffectWmPoiDeliveryInfo wmPoiId = {}", wmPoiId);

        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = wmPoiDeliveryInfoDBMapper.getByWmPoiId(wmPoiId);
        if (wmPoiDeliveryInfoDB == null) {
            return;
        }

        wmPoiDeliveryInfoDB.setStatus(PoiConstant.PoiModuleStatus.EFFECT.getCode());
        wmPoiDeliveryInfoDBMapper.updateSelective(wmPoiDeliveryInfoDB);

        WmPoiDeliveryInfo wmPoiDeliveryInfoAudited = WmPoiTransferUtil.transWmPoiDeliveryInfoDB2Bo(wmPoiDeliveryInfoDB);
        wmPoiDeliveryInfoAuditedService.saveOrUpdate(wmPoiDeliveryInfoAudited);

        wmPoiOpLogService.addLog(wmPoiId, PoiConstant.PoiModuleName.POI_DELIVERY_INFO, "设置门店配送信息生效", "系统()");
    }

    @Override
    public List<WmPoiDeliveryInfo> getByWmPoiIdList(List<Integer> wmPoiIdList) {
        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return Lists.newArrayList();
        }

        List<WmPoiDeliveryInfoDB> wmPoiDeliveryInfoDBList = wmPoiDeliveryInfoDBMapper.getByWmPoiIdList(wmPoiIdList);
        return WmPoiTransferUtil.transWmPoiDeliveryInfoDBList2BoList(wmPoiDeliveryInfoDBList);
    }

    @Override
    public void updateByIdForAudit(WmPoiDeliveryInfo wmPoiDeliveryInfo, String opUserId) {
        LOGGER.info("updateByIdForAudit wmPoiDeliveryInfo = {}, opUserId = {}", JSON.toJSONString(wmPoiDeliveryInfo), opUserId);
        if (wmPoiDeliveryInfo == null || StringUtils.isBlank(opUserId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = wmPoiDeliveryInfoDBMapper.getByWmPoiId(wmPoiDeliveryInfo.getWmPoiId());
        if (wmPoiDeliveryInfoDB == null) {
            throw new PoiException(PoiConstant.POI_OP_ERROR, "更新门店配送信息审核状态异常");
        }

        wmPoiDeliveryInfoDB.setStatus(wmPoiDeliveryInfo.getStatus());
        wmPoiDeliveryInfoDB.setAuditResult(wmPoiDeliveryInfo.getAuditResult());
        wmPoiDeliveryInfoDBMapper.updateSelective(wmPoiDeliveryInfoDB);

        String log = "审核结果:审核驳回:" + wmPoiDeliveryInfoDB.getAuditResult();
        wmPoiOpLogService.addLog(wmPoiDeliveryInfoDB.getWmPoiId(), PoiConstant.PoiModuleName.POI_DELIVERY_INFO, log, opUserId);
    }

    @Override
    public void deleteByWmPoiIdList(List<Integer> wmPoiIdList) {
        LOGGER.info("deleteByIdList wmPoiIdList = {}", JSON.toJSONString(wmPoiIdList));

        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return;
        }

        wmPoiDeliveryInfoDBMapper.deleteByWmPoiIdList(wmPoiIdList);
        wmPoiDeliveryInfoAuditedService.deleteByWmPoiIdList(wmPoiIdList);
    }
}
