package com.z.merchantsettle.modules.poi.service.impl;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.audit.constants.AuditApplicationTypeEnum;
import com.z.merchantsettle.modules.audit.constants.AuditConstant;
import com.z.merchantsettle.modules.audit.constants.AuditTypeEnum;
import com.z.merchantsettle.modules.audit.domain.bo.AuditTask;
import com.z.merchantsettle.modules.audit.domain.poi.AuditWmPoiBaseInfo;
import com.z.merchantsettle.modules.audit.service.ApiAuditService;
import com.z.merchantsettle.modules.base.domain.bo.CategoryInfo;
import com.z.merchantsettle.modules.base.domain.bo.CityInfo;
import com.z.merchantsettle.modules.base.service.BankService;
import com.z.merchantsettle.modules.base.service.CategoryService;
import com.z.merchantsettle.modules.base.service.GeoService;
import com.z.merchantsettle.modules.customer.service.CustomerPoiService;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiBaseInfoDBMapper;
import com.z.merchantsettle.modules.poi.domain.WmPoiSearchParam;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiBaseInfoDB;
import com.z.merchantsettle.modules.poi.service.WmPoiBaseInfoAuditedService;
import com.z.merchantsettle.modules.poi.service.WmPoiBaseInfoService;
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
public class WmPoiBaseInfoServiceImpl implements WmPoiBaseInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiBaseInfoServiceImpl.class);

    @Autowired
    private WmPoiBaseInfoDBMapper wmPoiBaseInfoDBMapper;

    @Autowired
    private ApiAuditService apiAuditService;

    @Autowired
    private WmPoiBaseInfoAuditedService wmPoiBaseInfoAuditedService;

    @Autowired
    private WmPoiOpLogService wmPoiOpLogService;

    @Autowired
    private GeoService geoService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerPoiService customerPoiService;


    @Override
    @Transactional
    public WmPoiBaseInfo saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo, String userId) {
        if (wmPoiBaseInfo == null || StringUtils.isBlank(userId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiBaseInfoDB wmPoiBaseInfoDB = WmPoiTransferUtil.transWmPoiBaseInfo2DB(wmPoiBaseInfo);
        boolean isNew = !(wmPoiBaseInfoDB.getId() != null && wmPoiBaseInfoDB.getId() > 0);
        if (isNew) {
            wmPoiBaseInfoDB.setWmPoiPrincipal(userId);
            wmPoiBaseInfoDB.setStatus(PoiConstant.PoiModuleStatus.AUDING.getCode());
            wmPoiBaseInfoDB.setCoopState(PoiConstant.PoiCoopState.COOPERATING.getCode());
            wmPoiBaseInfoDBMapper.insertSelective(wmPoiBaseInfoDB);

            customerPoiService.save(wmPoiBaseInfoDB.getCustomerId(), wmPoiBaseInfo.getId());
        } else {
            wmPoiBaseInfoDBMapper.updateSelective(wmPoiBaseInfoDB);
        }
        wmPoiBaseInfo = WmPoiTransferUtil.transWmPoiBaseInfoDB2Bo(wmPoiBaseInfoDB);

        wmPoiOpLogService.addLog(wmPoiBaseInfoDB.getId(), PoiConstant.PoiModuleName.POI_BASE_INFO, "保存门店基本信息", userId);
        commitAudit(wmPoiBaseInfo, isNew, userId);
        wmPoiOpLogService.addLog(wmPoiBaseInfoDB.getId(), PoiConstant.PoiModuleName.POI_BASE_INFO, "提交审核", userId);

        return wmPoiBaseInfo;
    }

    private void commitAudit(WmPoiBaseInfo wmPoiBaseInfo, boolean isNew, String userId) {
        AuditTask auditTask = new AuditTask();
        auditTask.setCustomerId(wmPoiBaseInfo.getCustomerId());
        auditTask.setPoiId(wmPoiBaseInfo.getId());
        auditTask.setAuditApplicationType(isNew ? AuditApplicationTypeEnum.AUDIT_NEW.getCode() : AuditApplicationTypeEnum.AUDIT_UPDATE.getCode());
        auditTask.setAuditStatus(AuditConstant.AuditStatus.AUDITING);
        auditTask.setAuditType(AuditTypeEnum.POI_BASE_INFO.getCode());
        auditTask.setSubmitterId(userId);

        AuditWmPoiBaseInfo auditWmPoiBaseInfo = new AuditWmPoiBaseInfo();
        TransferUtil.transferAll(wmPoiBaseInfo, auditWmPoiBaseInfo);
        auditWmPoiBaseInfo.setRecordId(wmPoiBaseInfo.getId());
        auditWmPoiBaseInfo.setWmPoiId(wmPoiBaseInfo.getId());

        CityInfo cityInfo = geoService.getByProvinceIdAndCityId(wmPoiBaseInfo.getWmPoiCityId(), wmPoiBaseInfo.getWmPoiRegionId());
        String cityRegion = cityInfo == null ? "" : cityInfo.getProvinceName() + "-" + cityInfo.getCityName();
        auditWmPoiBaseInfo.setWmPoiCityRegion(cityRegion);

        CategoryInfo categoryInfo = categoryService.getById(wmPoiBaseInfo.getWmPoiCategory());
        auditWmPoiBaseInfo.setWmPoiCategory(categoryInfo == null ? "" : categoryInfo.getCategoryName());

        auditTask.setAuditData(JSON.toJSONString(auditWmPoiBaseInfo));
        apiAuditService.commitAudit(auditTask);
    }


    @Override
    public WmPoiBaseInfo getWmPoiBaseInfoById(Integer wmPoiId, Integer effective) {
        if (wmPoiId == null || wmPoiId <= 0 || effective < 0) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiBaseInfo wmPoiBaseInfo;
        if (PoiConstant.EFFECTIVE == effective) {
            wmPoiBaseInfo = wmPoiBaseInfoAuditedService.getWmPoiBaseInfoById(wmPoiId);
        } else {
            WmPoiBaseInfoDB wmPoiBaseInfoDB = wmPoiBaseInfoDBMapper.getById(wmPoiId);
            wmPoiBaseInfo = WmPoiTransferUtil.transWmPoiBaseInfoDB2Bo(wmPoiBaseInfoDB);
        }
        return wmPoiBaseInfo;
    }

    @Override
    @Transactional
    public void setupEffectWmPoiBaseInfo(Integer wmPoiId) {
        LOGGER.info("setupEffectWmPoiBaseInfo wmPoiId = {}", wmPoiId);

        WmPoiBaseInfoDB wmPoiBaseInfoDB = wmPoiBaseInfoDBMapper.getById(wmPoiId);
        if (wmPoiBaseInfoDB == null) {
            return;
        }

        wmPoiBaseInfoDB.setStatus(PoiConstant.PoiModuleStatus.EFFECT.getCode());
        wmPoiBaseInfoDBMapper.updateSelective(wmPoiBaseInfoDB);

        WmPoiBaseInfo wmPoiBaseInfo = WmPoiTransferUtil.transWmPoiBaseInfoDB2Bo(wmPoiBaseInfoDB);
        wmPoiBaseInfoAuditedService.saveOrUpdate(wmPoiBaseInfo);

        customerPoiService.saveAudited(wmPoiBaseInfoDB.getCustomerId(), wmPoiBaseInfoDB.getId());
        wmPoiOpLogService.addLog(wmPoiBaseInfoDB.getId(), PoiConstant.PoiModuleName.POI_BASE_INFO, "设置门店基本信息生效", "系统()");
    }

    @Override
    public List<WmPoiBaseInfo> getByIdList(List<Integer> wmPoiIdList) {
        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return Lists.newArrayList();
        }

        List<WmPoiBaseInfoDB> wmPoiBaseInfoDBList = wmPoiBaseInfoDBMapper.getByIdList(wmPoiIdList);
        return WmPoiTransferUtil.transWmPoiBaseInfoDBList2BoList(wmPoiBaseInfoDBList);
    }

    @Override
    public void distributePrincipal(Integer wmPoiId, String principalId, String opUserId) {
        if (wmPoiId == null || wmPoiId <= 0 || StringUtils.isBlank(principalId) || StringUtils.isBlank(opUserId)) {
            throw new PoiException(PoiConstant.POI_OP_ERROR, "参数错误");
        }

        WmPoiBaseInfoDB wmPoiBaseInfoDB = wmPoiBaseInfoDBMapper.getById(wmPoiId);
        wmPoiBaseInfoDB.setWmPoiPrincipal(principalId);
        wmPoiBaseInfoDBMapper.updateSelective(wmPoiBaseInfoDB);

        WmPoiBaseInfo wmPoiBaseInfo = WmPoiTransferUtil.transWmPoiBaseInfoDB2Bo(wmPoiBaseInfoDB);
        wmPoiBaseInfoAuditedService.saveOrUpdate(wmPoiBaseInfo);

        String diffContent = String.format("门店责任人变更: %s", principalId);
        wmPoiOpLogService.addLog(wmPoiId, PoiConstant.PoiModuleName.POI_BASE_INFO, diffContent, opUserId);
    }

    @Override
    public PageData<WmPoiBaseInfo> getBaseInfoList(WmPoiSearchParam wmPoiSearchParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<WmPoiBaseInfoDB> wmPoiBaseInfoDBList = wmPoiBaseInfoDBMapper.getList(wmPoiSearchParam);
        LOGGER.info("getBaseInfoList wmPoiBaseInfoDBList = {}", JSON.toJSONString(wmPoiBaseInfoDBList));
        PageInfo<WmPoiBaseInfoDB> pageInfo = new PageInfo<>(wmPoiBaseInfoDBList);
        List<WmPoiBaseInfo> wmPoiBaseInfoList = WmPoiTransferUtil.transWmPoiBaseInfoDBList2BoList(wmPoiBaseInfoDBList);

        return new PageData.Builder<WmPoiBaseInfo>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPages())
                .data(wmPoiBaseInfoList)
                .build();
    }

    @Override
    public void updateByIdForAudit(WmPoiBaseInfo wmPoiBaseInfo, String opUserId) {
        LOGGER.info("updateByIdForAudit wmPoiBaseInfo = {}, opUserId = {}", JSON.toJSONString(wmPoiBaseInfo), opUserId);
        if (wmPoiBaseInfo == null || StringUtils.isBlank(opUserId)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiBaseInfoDB wmPoiBaseInfoDB = wmPoiBaseInfoDBMapper.getById(wmPoiBaseInfo.getId());
        if (wmPoiBaseInfoDB == null) {
            throw new PoiException(PoiConstant.POI_OP_ERROR, "更新门店基本信息审核状态异常");
        }

        wmPoiBaseInfoDB.setStatus(wmPoiBaseInfo.getStatus());
        wmPoiBaseInfoDB.setAuditResult(wmPoiBaseInfo.getAuditResult());
        wmPoiBaseInfoDBMapper.updateSelective(wmPoiBaseInfoDB);

        String log = "审核结果:审核驳回:" + wmPoiBaseInfoDB.getAuditResult();
        wmPoiOpLogService.addLog(wmPoiBaseInfoDB.getId(), PoiConstant.PoiModuleName.POI_BASE_INFO, log, opUserId);
    }

    @Override
    public void deleteByIdList(List<Integer> wmPoiIdList) {
        LOGGER.info("deleteByIdList wmPoiIdList = {}", JSON.toJSONString(wmPoiIdList));

        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return;
        }

        wmPoiBaseInfoDBMapper.deleteByIdList(wmPoiIdList);
        wmPoiBaseInfoAuditedService.deleteByIdList(wmPoiIdList);
    }
}
