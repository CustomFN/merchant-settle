package com.z.merchantsettle.modules.poi.service.impl;

import cn.hutool.poi.exceptions.POIException;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.CertificatesTypeEnum;
import com.z.merchantsettle.exception.CustomerException;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.audit.constants.AuditApplicationTypeEnum;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.poi.AuditWmPoiQua;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiQuaDBMapper;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQua;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiQuaDB;
import com.z.merchantsettle.modules.poi.service.WmPoiOpLogService;
import com.z.merchantsettle.modules.poi.service.WmPoiQuaAuditedService;
import com.z.merchantsettle.modules.poi.service.WmPoiQuaService;
import com.z.merchantsettle.utils.TransferUtil;
import com.z.merchantsettle.utils.aliyun.AliyunUtil;
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
    @Transactional
    public WmPoiQua saveOrUpdate(WmPoiQua wmPoiQua, String userId) {
        if (wmPoiQua == null || StringUtils.isBlank(userId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }
        if (false) {
            checkWmPoiQua(wmPoiQua);
        }

        WmPoiQuaDB wmPoiQuaDB = WmPoiTransferUtil.transWmPoiQua2DB(wmPoiQua);
        boolean isNew = !(wmPoiQuaDB.getId() != null && wmPoiQuaDB.getId() > 0);
        if (isNew) {
            wmPoiQuaDB.setStatus(PoiConstant.PoiModuleStatus.AUDING.getCode());
            wmPoiQuaDBMapper.insertSelective(wmPoiQuaDB);
        } else {
            wmPoiQuaDBMapper.updateSelective(wmPoiQuaDB);
        }
        wmPoiQua = WmPoiTransferUtil.transWmPoiQuaDB2Bo(wmPoiQuaDB);
        wmPoiOpLogService.addLog(wmPoiQua.getWmPoiId(), PoiConstant.PoiModuleName.POI_QUA, "保存门店资质信息", userId);
        commitAudit(wmPoiQua, isNew, userId);
        wmPoiOpLogService.addLog(wmPoiQua.getWmPoiId(), PoiConstant.PoiModuleName.POI_QUA, "门店资质信息提交审核成功", userId);
        return wmPoiQua;
    }

    private void checkWmPoiQua(WmPoiQua wmPoiQua) {
        boolean validResult = true;
        validResult = AliyunUtil.iDCardValid(wmPoiQua.getWmPoiLinkManIDCardNo(), wmPoiQua.getWmPoiLinkManName());
        if (!validResult) {
            throw new PoiException(PoiConstant.POI_VALID_ERROR, "门店资质个人证件验证失败,请重新确认!");
        }

        validResult = AliyunUtil.enterpriseValid(wmPoiQua.getWmPoiBusinessLicenceNo(),
                wmPoiQua.getWmPoiBusinessLicenceName(), wmPoiQua.getWmPoiOperatorName());
        if (!validResult) {
            throw new PoiException(PoiConstant.POI_VALID_ERROR, "门店资质营业执照验证失败,请重新确认!");
        }
    }

    private void commitAudit(WmPoiQua wmPoiQua, boolean isNew, String userId) {
        AuditTask auditTask = new AuditTask();
        auditTask.setPoiId(wmPoiQua.getWmPoiId());
        auditTask.setAuditApplicationType(isNew ? AuditApplicationTypeEnum.AUDIT_NEW.getCode() : AuditApplicationTypeEnum.AUDIT_UPDATE.getCode());
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditTypeEnum.POI_QUA_INFO.getCode());
        auditTask.setSubmitterId(userId);

        AuditWmPoiQua auditWmPoiQua = new AuditWmPoiQua();
        TransferUtil.transferAll(wmPoiQua, auditWmPoiQua);
        auditWmPoiQua.setRecordId(wmPoiQua.getId());
        auditWmPoiQua.setWmPoiId(wmPoiQua.getWmPoiId());
        auditWmPoiQua.setWmPoiLinkManIDCardType(CertificatesTypeEnum.getByCode(wmPoiQua.getWmPoiLinkManIDCardType()));
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
            wmPoiQua = wmPoiQuaAuditedService.getWmPoiQuaAuditedById(wmPoiId);
        } else {
            WmPoiQuaDB wmPoiQuaDB = wmPoiQuaDBMapper.getByWmPoiId(wmPoiId);
            wmPoiQua = WmPoiTransferUtil.transWmPoiQuaDB2Bo(wmPoiQuaDB);
        }
        return wmPoiQua;
    }


    @Override
    @Transactional
    public void setupEffectWmPoiQua(Integer wmPoiId) {
        LOGGER.info("setupEffectWmPoiQua wmPoiId = {}", wmPoiId);

        WmPoiQuaDB wmPoiQuaDB = wmPoiQuaDBMapper.getByWmPoiId(wmPoiId);
        if (wmPoiQuaDB == null) {
            return;
        }

        wmPoiQuaDB.setStatus(PoiConstant.PoiModuleStatus.EFFECT.getCode());
        wmPoiQuaDBMapper.updateSelective(wmPoiQuaDB);

        WmPoiQua wmPoiQuaAudited = WmPoiTransferUtil.transWmPoiQuaDB2Bo(wmPoiQuaDB);
        wmPoiQuaAuditedService.saveOrUpdate(wmPoiQuaAudited);

        wmPoiOpLogService.addLog(wmPoiId, PoiConstant.PoiModuleName.POI_QUA, "设置门店资质信息生效", "系统()");
    }

    @Override
    public List<WmPoiQua> getByWmPoiIdList(List<Integer> wmPoiIdList) {
        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return Lists.newArrayList();
        }
        List<WmPoiQuaDB> wmPoiQuaDBList = wmPoiQuaDBMapper.getByWmPoiIdList(wmPoiIdList);
        return WmPoiTransferUtil.transWmPoiQuaDBList2BoList(wmPoiQuaDBList);
    }

    @Override
    public void updateByIdForAudit(WmPoiQua wmPoiQua, String opUserId) {
        LOGGER.info("updateByIdForAudit wmPoiQua = {}, opUserId = {}", JSON.toJSONString(wmPoiQua), opUserId);
        if (wmPoiQua == null || StringUtils.isBlank(opUserId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiQuaDB wmPoiQuaDB = wmPoiQuaDBMapper.getByWmPoiId(wmPoiQua.getWmPoiId());
        if (wmPoiQuaDB == null) {
            throw new PoiException(PoiConstant.POI_OP_ERROR, "更新门店资质信息审核状态异常");
        }

        wmPoiQuaDB.setStatus(wmPoiQua.getStatus());
        wmPoiQuaDB.setAuditResult(wmPoiQua.getAuditResult());
        wmPoiQuaDBMapper.updateSelective(wmPoiQuaDB);

        String log = "审核结果:审核驳回:" + wmPoiQuaDB.getAuditResult();
        wmPoiOpLogService.addLog(wmPoiQuaDB.getWmPoiId(), PoiConstant.PoiModuleName.POI_QUA, log, opUserId);
    }

    @Override
    public void deleteByWmPoiIdList(List<Integer> wmPoiIdList) {
        LOGGER.info("deleteByIdList wmPoiIdList = {}", JSON.toJSONString(wmPoiIdList));

        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return;
        }
        wmPoiQuaDBMapper.deleteByWmPoiIdList(wmPoiIdList);
        wmPoiQuaAuditedService.deleteByWmPoiIdList(wmPoiIdList);
    }
}
