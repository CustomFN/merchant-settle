package com.z.merchantsettle.modules.poi.service.impl;

import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiDeliveryInfoAuditedDBMapper;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfoAudited;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiDeliveryInfoDB;
import com.z.merchantsettle.modules.poi.service.WmPoiDeliveryInfoAuditedService;
import com.z.merchantsettle.utils.transfer.poi.WmPoiTransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WmPoiDeliveryInfoAuditedServiceImpl implements WmPoiDeliveryInfoAuditedService {

    @Autowired
    private WmPoiDeliveryInfoAuditedDBMapper wmPoiDeliveryInfoAuditedDBMapper;


    @Override
    public void saveOrUpdate(WmPoiDeliveryInfoAudited wmPoiDeliveryInfoAudited) {
        if (wmPoiDeliveryInfoAudited == null) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiDeliveryInfoAudited wmPoiDeliveryInfoAuditedInDB = getWmPoiDeliveryInfoAuditedById(wmPoiDeliveryInfoAudited.getWmPoiId());
        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = WmPoiTransferUtil.transWmPoiDeliveryInfoAudited2DB(wmPoiDeliveryInfoAudited);
        if (wmPoiDeliveryInfoAuditedInDB == null) {
            wmPoiDeliveryInfoAuditedDBMapper.insertSelective(wmPoiDeliveryInfoDB);
        } else {
            wmPoiDeliveryInfoAuditedDBMapper.updateSelective(wmPoiDeliveryInfoDB);
        }
    }

    @Override
    public WmPoiDeliveryInfoAudited getWmPoiDeliveryInfoAuditedById(Integer wmPoiId) {
        if (wmPoiId == null || wmPoiId <= 0) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        return WmPoiTransferUtil.transWmPoiDeliveryInfoDB2Audited(wmPoiDeliveryInfoAuditedDBMapper.getByWmPoiId(wmPoiId));
    }
}
