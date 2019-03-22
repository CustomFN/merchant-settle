package com.z.merchantsettle.modules.poi.service.impl;

import cn.hutool.poi.exceptions.POIException;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiQuaAuditedDBMapper;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQua;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiQuaDB;
import com.z.merchantsettle.modules.poi.service.WmPoiQuaAuditedService;
import com.z.merchantsettle.utils.transfer.poi.WmPoiTransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WmPoiQuaAuditedServiceImpl implements WmPoiQuaAuditedService {

    @Autowired
    private WmPoiQuaAuditedDBMapper wmPoiQuaAuditedDBMapper;

    @Override
    public void saveOrUpdate(WmPoiQua wmPoiQua) {
        if (wmPoiQua == null) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiQua wmPoiQuaAuditedInDB = getWmPoiQuaAuditedById(wmPoiQua.getId());
        WmPoiQuaDB wmPoiQuaDB = WmPoiTransferUtil.transWmPoiQua2DB(wmPoiQua);
        if (wmPoiQuaAuditedInDB == null) {
            wmPoiQuaAuditedDBMapper.insertSelective(wmPoiQuaDB);
        } else {
            wmPoiQuaAuditedDBMapper.updateSelective(wmPoiQuaDB);
        }
    }

    @Override
    public WmPoiQua getWmPoiQuaAuditedById(Integer wmPoiId) throws POIException {
        if (wmPoiId == null || wmPoiId <= 0) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        return WmPoiTransferUtil.transWmPoiQuaDB2Bo(wmPoiQuaAuditedDBMapper.getById(wmPoiId));
    }
}
