package com.z.merchantsettle.modules.poi.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiBaseInfoAuditedDBMapper;
import com.z.merchantsettle.modules.poi.domain.WmPoiSearchParam;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiBaseInfoDB;
import com.z.merchantsettle.modules.poi.service.WmPoiBaseInfoAuditedService;
import com.z.merchantsettle.utils.transfer.poi.WmPoiTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WmPoiBaseInfoAuditedServiceImpl implements WmPoiBaseInfoAuditedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiBaseInfoAuditedServiceImpl.class);

    @Autowired
    private WmPoiBaseInfoAuditedDBMapper wmPoiBaseInfoAuditedDBMapper;

    @Override
    public void saveOrUpdate(WmPoiBaseInfo wmPoiBaseInfo) {
        if (wmPoiBaseInfo == null) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiBaseInfo wmPoiBaseInfoInDB = getWmPoiBaseInfoById(wmPoiBaseInfo.getId());
        WmPoiBaseInfoDB wmPoiBaseInfoDB = WmPoiTransferUtil.transWmPoiBaseInfo2DB(wmPoiBaseInfo);
        if (wmPoiBaseInfoInDB == null) {
            wmPoiBaseInfoAuditedDBMapper.insertSelective(wmPoiBaseInfoDB);
        } else {
            wmPoiBaseInfoAuditedDBMapper.updateSelective(wmPoiBaseInfoDB);
        }

    }

    @Override
    public WmPoiBaseInfo getWmPoiBaseInfoById(Integer wmPoiId) {
        if (wmPoiId == null || wmPoiId <= 0) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        return WmPoiTransferUtil.transWmPoiBaseInfoDB2Bo(wmPoiBaseInfoAuditedDBMapper.getById(wmPoiId));
    }

    @Override
    public void deleteByIdList(List<Integer> wmPoiIdList) {
        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        wmPoiBaseInfoAuditedDBMapper.deleteByIdList(wmPoiIdList);
    }


}
