package com.z.merchantsettle.modules.poi.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WmPoiBaseInfoAuditedServiceImpl implements WmPoiBaseInfoAuditedService {

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
    public PageData<WmPoiBaseInfo> getBaseInfoList(WmPoiSearchParam wmPoiSearchParam, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<WmPoiBaseInfoDB> wmPoiBaseInfoDBList = wmPoiBaseInfoAuditedDBMapper.getList(wmPoiSearchParam);
        List<WmPoiBaseInfo> wmPoiBaseInfoList = WmPoiTransferUtil.transWmPoiBaseInfoDBList2BoList(wmPoiBaseInfoDBList);
        PageInfo<WmPoiBaseInfo> pageInfo = new PageInfo<>(wmPoiBaseInfoList);

        return new PageData.Builder<WmPoiBaseInfo>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalSize((int) pageInfo.getTotal())
                .totalPage(pageInfo.getPages())
                .data(wmPoiBaseInfoList)
                .build();
    }
}
