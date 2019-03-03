package com.z.merchantsettle.modules.poi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.poi.AuditWmPoiDeliveryInfo;
import com.z.merchantsettle.modules.audit.domain.poi.AuditWmPoiProject;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiDeliveryInfoDBMapper;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfo;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfoAudited;
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
    public void saveOrUpdate(WmPoiDeliveryInfo wmPoiDeliveryInfo, String opUserId) {
        if (wmPoiDeliveryInfo == null || StringUtils.isBlank(opUserId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = WmPoiTransferUtil.transWmPoiDeliveryInfo2DB(wmPoiDeliveryInfo);
        boolean isNew = !(wmPoiDeliveryInfoDB.getId() != null && wmPoiDeliveryInfoDB.getId() > 0);
        if (isNew) {
            wmPoiDeliveryInfoDBMapper.updateSelective(wmPoiDeliveryInfoDB);
        } else {
            wmPoiDeliveryInfoDBMapper.insertSelective(wmPoiDeliveryInfoDB);
        }
        wmPoiOpLogService.addLog(wmPoiDeliveryInfo.getWmPoiId(), PoiConstant.PoiModuleName.POI_DELIVERY_INFO, "保存门店配送信息", opUserId);

        commitAudit(wmPoiDeliveryInfoDB, isNew, opUserId);
        wmPoiOpLogService.addLog(wmPoiDeliveryInfo.getWmPoiId(), PoiConstant.PoiModuleName.POI_DELIVERY_INFO, "门店配送信息提交审核成功", opUserId);
    }

    private void commitAudit(WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB, boolean isNew, String opUserId) {
        AuditTask auditTask = new AuditTask();
        auditTask.setPoiId(wmPoiDeliveryInfoDB.getWmPoiId());
        auditTask.setAuditApplicationType(isNew ? AuditConstant.AuditApplicationType.AUDIT_NEW : AuditConstant.AuditApplicationType.AUDIT_UPDATE);
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditConstant.AuditType.CUSTOMER);
        auditTask.setSubmitterId(opUserId);

        AuditWmPoiDeliveryInfo auditWmPoiDeliveryInfo = new AuditWmPoiDeliveryInfo();
        TransferUtil.transferAll(wmPoiDeliveryInfoDB, auditWmPoiDeliveryInfo);
        auditWmPoiDeliveryInfo.setWmPoiProjectList(JSONArray.parseArray(wmPoiDeliveryInfoDB.getWmPoiProjects(), AuditWmPoiProject.class));
        auditWmPoiDeliveryInfo.setRecordId(wmPoiDeliveryInfoDB.getId());
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
            WmPoiDeliveryInfoAudited wmPoiDeliveryInfoAudited  = wmPoiDeliveryInfoAuditedService.getWmPoiDeliveryInfoAuditedById(wmPoiId);
            wmPoiDeliveryInfo = transWmPoiDeliveryInfoAudited2Info(wmPoiDeliveryInfoAudited);
        } else {
            WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = wmPoiDeliveryInfoDBMapper.getByWmPoiId(wmPoiId);
            wmPoiDeliveryInfo = WmPoiTransferUtil.transWmPoiDeliveryInfoDB2Bo(wmPoiDeliveryInfoDB);
        }
        return wmPoiDeliveryInfo;
    }

    private WmPoiDeliveryInfo transWmPoiDeliveryInfoAudited2Info(WmPoiDeliveryInfoAudited wmPoiDeliveryInfoAudited) {
        if (wmPoiDeliveryInfoAudited == null) {
            return null;
        }

        WmPoiDeliveryInfo wmPoiDeliveryInfo = new WmPoiDeliveryInfo();
        TransferUtil.transferAll(wmPoiDeliveryInfoAudited, wmPoiDeliveryInfo);
        return wmPoiDeliveryInfo;
    }

    @Override
    public void setupEffectWmPoiDeliveryInfo(Integer wmPoiId, String opUserId) {
        LOGGER.info("setupEffectWmPoiDeliveryInfo wmPoiId = {}", wmPoiId);

        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = wmPoiDeliveryInfoDBMapper.getByWmPoiId(wmPoiId);
        if (wmPoiDeliveryInfoDB == null) {
            return;
        }

        wmPoiDeliveryInfoDB.setStatus(PoiConstant.PoiModuleStatus.EFFECT);
        wmPoiDeliveryInfoDBMapper.updateSelective(wmPoiDeliveryInfoDB);

        WmPoiDeliveryInfoAudited wmPoiDeliveryInfoAudited = WmPoiTransferUtil.transWmPoiDeliveryInfoDB2Audited(wmPoiDeliveryInfoDB);
        wmPoiDeliveryInfoAuditedService.saveOrUpdate(wmPoiDeliveryInfoAudited);

        wmPoiOpLogService.addLog(wmPoiId, PoiConstant.PoiModuleName.POI_DELIVERY_INFO, "设置门店配送信息生效", opUserId);
    }

    @Override
    public List<WmPoiDeliveryInfo> getByWmPoiIdList(List<Integer> wmPoiIdList) {
        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return Lists.newArrayList();
        }

        List<WmPoiDeliveryInfoDB> wmPoiDeliveryInfoDBList = wmPoiDeliveryInfoDBMapper.getByWmPoiIdList(wmPoiIdList);
        return WmPoiTransferUtil.transWmPoiDeliveryInfoDBList2BoList(wmPoiDeliveryInfoDBList);
    }
}
