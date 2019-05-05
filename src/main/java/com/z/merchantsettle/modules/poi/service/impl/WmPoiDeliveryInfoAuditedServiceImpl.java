package com.z.merchantsettle.modules.poi.service.impl;

import com.z.merchantsettle.exception.PoiException;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.dao.WmPoiDeliveryInfoAuditedDBMapper;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfo;
import com.z.merchantsettle.modules.poi.domain.db.WmPoiDeliveryInfoDB;
import com.z.merchantsettle.modules.poi.service.WmPoiDeliveryInfoAuditedService;
import com.z.merchantsettle.utils.transfer.poi.WmPoiTransferUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WmPoiDeliveryInfoAuditedServiceImpl implements WmPoiDeliveryInfoAuditedService {

    @Autowired
    private WmPoiDeliveryInfoAuditedDBMapper wmPoiDeliveryInfoAuditedDBMapper;


    @Override
    public void saveOrUpdate(WmPoiDeliveryInfo wmPoiDeliveryInfoAudited) {
        if (wmPoiDeliveryInfoAudited == null) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        WmPoiDeliveryInfo wmPoiDeliveryInfoAuditedInDB = getWmPoiDeliveryInfoAuditedById(wmPoiDeliveryInfoAudited.getWmPoiId());
        WmPoiDeliveryInfoDB wmPoiDeliveryInfoDB = WmPoiTransferUtil.transWmPoiDeliveryInfo2DB(wmPoiDeliveryInfoAudited);
        if (wmPoiDeliveryInfoAuditedInDB == null) {
            wmPoiDeliveryInfoAuditedDBMapper.insertSelective(wmPoiDeliveryInfoDB);
        } else {
            wmPoiDeliveryInfoAuditedDBMapper.updateSelective(wmPoiDeliveryInfoDB);
        }
    }

    @Override
    public WmPoiDeliveryInfo getWmPoiDeliveryInfoAuditedById(Integer wmPoiId) {
        if (wmPoiId == null || wmPoiId <= 0) {
            throw new PoiException(PoiConstant.POI_PARAM_ERROR, "参数错误");
        }

        return WmPoiTransferUtil.transWmPoiDeliveryInfoDB2Bo(wmPoiDeliveryInfoAuditedDBMapper.getByWmPoiId(wmPoiId));
    }

    @Override
    public void deleteByWmPoiIdList(List<Integer> wmPoiIdList) {
        if (CollectionUtils.isEmpty(wmPoiIdList)) {
            return;
        }

        wmPoiDeliveryInfoAuditedDBMapper.deleteByWmPoiIdList(wmPoiIdList);
    }
}
