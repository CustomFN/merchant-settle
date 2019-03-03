package com.z.merchantsettle.modules.poi.service.impl;

import cn.hutool.poi.exceptions.POIException;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.poi.AuditWmPoiQua;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiQuaDBMapper;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQua;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQuaAudited;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiQuaDB;
import com.z.merchantsettle.modules.poi.service.WmPoiOpLogService;
import com.z.merchantsettle.modules.poi.service.WmPoiQuaAuditedService;
import com.z.merchantsettle.modules.poi.service.WmPoiQuaService;
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
public class WmPoiQuaServiceImpl implements WmPoiQuaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiQuaServiceImpl.class);

    @Autowired
    private WmPoiQuaDBMapper wmPoiQuaDBMapper;

    @Autowired
    private WmPoiQuaAuditedService wmPoiQuaAuditedService;

    @Autowired
    private ApiAuditService apiAuditService;

    @Autowired
    private WmPoiOpLogService wmPoiOpLogService;


    @Override
    public void saveOrUpdate(WmPoiQua wmPoiQua, String userId) {
        if (wmPoiQua == null || StringUtils.isBlank(userId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiQuaDB wmPoiQuaDB = WmPoiTransferUtil.transWmPoiQua2DB(wmPoiQua);
        boolean isNew = !(wmPoiQuaDB.getId() != null && wmPoiQuaDB.getId() > 0);
        if (isNew) {
            wmPoiQuaDBMapper.updateSelective(wmPoiQuaDB);
        } else {
            wmPoiQuaDBMapper.insertSelective(wmPoiQuaDB);
        }
        wmPoiOpLogService.addLog(wmPoiQua.getWmPoiId(), PoiConstant.PoiModuleName.POI_QUA, "保存门店资质信息", userId);

        commitAudit(wmPoiQuaDB, isNew, userId);
        wmPoiOpLogService.addLog(wmPoiQua.getWmPoiId(), PoiConstant.PoiModuleName.POI_QUA, "门店资质信息提交审核成功", userId);
    }

    private void commitAudit(WmPoiQuaDB wmPoiQuaDB, boolean isNew, String userId) {
        AuditTask auditTask = new AuditTask();
        auditTask.setPoiId(wmPoiQuaDB.getWmPoiId());
        auditTask.setAuditApplicationType(isNew ? AuditConstant.AuditApplicationType.AUDIT_NEW : AuditConstant.AuditApplicationType.AUDIT_UPDATE);
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditConstant.AuditType.CUSTOMER);
        auditTask.setSubmitterId(userId);

        AuditWmPoiQua auditWmPoiQua = new AuditWmPoiQua();
        TransferUtil.transferAll(wmPoiQuaDB, auditWmPoiQua);
        auditWmPoiQua.setRecordId(wmPoiQuaDB.getId());
        auditTask.setAuditData(JSON.toJSONString(auditWmPoiQua));
        apiAuditService.commitAudit(auditTask);
    }


    @Override
    public WmPoiQua getWmPoiQuaByWmPoiId(Integer wmPoiId, Integer effective) throws POIException {
        if (wmPoiId == null || wmPoiId <= 0 || effective < 0) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiQua wmPoiQua;
        if (PoiConstant.EFFECTIVE == effective) {
            WmPoiQuaAudited wmPoiQuaAudited = wmPoiQuaAuditedService.getWmPoiQuaAuditedById(wmPoiId);
            wmPoiQua = transWmPoiQuaAudited2WmPoiQua(wmPoiQuaAudited);
        } else {
            WmPoiQuaDB wmPoiQuaDB = wmPoiQuaDBMapper.getByWmPoiId(wmPoiId);
            wmPoiQua = WmPoiTransferUtil.transWmPoiQuaDB2Bo(wmPoiQuaDB);
        }
        return wmPoiQua;
    }


    private WmPoiQua transWmPoiQuaAudited2WmPoiQua(WmPoiQuaAudited wmPoiQuaAudited) {
        if (wmPoiQuaAudited == null) {
            return null;
        }

        WmPoiQua wmPoiQua = new WmPoiQua();
        TransferUtil.transferAll(wmPoiQuaAudited, wmPoiQua);
        return wmPoiQua;
    }


    @Override
    public void setupEffectWmPoiQua(Integer wmPoiId, String opUserId) {
        LOGGER.info("setupEffectWmPoiQua wmPoiId = {}", wmPoiId);

        WmPoiQuaDB wmPoiQuaDB = wmPoiQuaDBMapper.getByWmPoiId(wmPoiId);
        if (wmPoiQuaDB == null) {
            return;
        }

        wmPoiQuaDB.setStatus(PoiConstant.PoiModuleStatus.EFFECT);
        wmPoiQuaDBMapper.updateSelective(wmPoiQuaDB);

        WmPoiQuaAudited wmPoiQuaAudited = WmPoiTransferUtil.transWmPoiQuaDB2Audited(wmPoiQuaDB);
        wmPoiQuaAuditedService.saveOrUpdate(wmPoiQuaAudited);

        wmPoiOpLogService.addLog(wmPoiId, PoiConstant.PoiModuleName.POI_QUA, "设置门店资质信息生效", opUserId);
    }

    @Override
    public List<WmPoiQua> getByWmPoiIdList(List<Integer> wmPoiIdList) {
        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return Lists.newArrayList();
        }
        List<WmPoiQuaDB> wmPoiQuaDBList = wmPoiQuaDBMapper.getByWmPoiIdList(wmPoiIdList);
        return WmPoiTransferUtil.transWmPoiQuaDBList2BoList(wmPoiQuaDBList);
    }
}
