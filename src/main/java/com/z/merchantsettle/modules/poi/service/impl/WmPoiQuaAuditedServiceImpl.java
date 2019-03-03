package com.z.merchantsettle.modules.poi.service.impl;

import cn.hutool.poi.exceptions.POIException;
import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiQuaAuditedDBMapper;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQuaAudited;
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
    public void saveOrUpdate(WmPoiQuaAudited wmPoiQuaAudited) {
        if (wmPoiQuaAudited == null) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiQuaAudited wmPoiQuaAuditedInDB = getWmPoiQuaAuditedById(wmPoiQuaAudited.getId());
        WmPoiQuaDB wmPoiQuaDB = WmPoiTransferUtil.transWmPoiQuaAudited2DB(wmPoiQuaAudited);
        if (wmPoiQuaAuditedInDB == null) {
            wmPoiQuaAuditedDBMapper.insertSelective(wmPoiQuaDB);
        } else {
            wmPoiQuaAuditedDBMapper.updateSelective(wmPoiQuaDB);
        }
    }

    @Override
    public WmPoiQuaAudited getWmPoiQuaAuditedById(Integer wmPoiId) throws POIException {
        if (wmPoiId == null || wmPoiId <= 0) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        return WmPoiTransferUtil.transWmPoiQuaDB2Audited(wmPoiQuaAuditedDBMapper.getById(wmPoiId));
    }
}
