package com.z.merchantsettle.modules.poi.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.z.merchantsettle.common.PageData;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettle;
import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettlePoi;
import com.z.merchantsettle.modules.customer.service.CustomerSettlePoiService;
import com.z.merchantsettle.modules.customer.service.CustomerSettleService;
import com.z.merchantsettle.modules.poi.constants.PoiConstant;
import com.z.merchantsettle.modules.poi.domain.WmPoiSearchParam;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiBaseInfo;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiDeliveryInfo;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiInfo;
import com.z.merchantsettle.modules.poi.domain.bo.WmPoiQua;
import com.z.merchantsettle.modules.poi.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WmPoiServiceImpl implements WmPoiService {

    @Autowired
    private WmPoiBaseInfoService wmPoiBaseInfoService;

    @Autowired
    private WmPoiBaseInfoAuditedService wmPoiBaseInfoAuditedService;

    @Autowired
    private WmPoiQuaService wmPoiQuaService;

    @Autowired
    private WmPoiDeliveryInfoService wmPoiDeliveryInfoService;

    @Autowired
    private CustomerSettlePoiService customerSettlePoiService;

    @Autowired
    private CustomerSettleService customerSettleService;

    private static final String POI_BASE_INFO = "baseInfo";

    private static final String POI_BUSINESS_INFO = "businessInfo";

    @Override
    public PageData<WmPoiInfo> getBaseList(WmPoiSearchParam wmPoiSearchParam, Integer pageNum, Integer pageSize) {
        PageData<WmPoiBaseInfo> pageData =  wmPoiBaseInfoAuditedService.getBaseInfoList(wmPoiSearchParam, pageNum, pageSize);
        List<WmPoiBaseInfo> wmPoiBaseInfoList = pageData.getData();
        if (CollectionUtils.isEmpty(wmPoiBaseInfoList)) {
            return new PageData<WmPoiInfo>();
        }

        List<Integer> wmPoiIdList = Lists.transform(wmPoiBaseInfoList, new Function<WmPoiBaseInfo, Integer>() {
            @Override
            public Integer apply(WmPoiBaseInfo input) {
                return input.getId();
            }
        });

        Map<String, Map<Integer, Integer>> wmPoiBaseMap = getWmPoiBaseMap(wmPoiIdList);
        Map<Integer, Integer> wmPoiBaseInfoStatusMap = wmPoiBaseMap.get(POI_BASE_INFO);
        Map<Integer, Integer> wmPoiQuaStatusMap = getWmPoiQuaStatusMap(wmPoiIdList);
        Map<Integer, Integer> wmPoiSettleStatusMap = getWmPoiSettleStatusMap(wmPoiIdList);
        Map<Integer, Integer> wmPoiDeliveryStatusMap = getWmPoiDeliveryStatusMap(wmPoiIdList);
        Map<Integer, Integer> wmPoiBusinessStatusMap = wmPoiBaseMap.get(POI_BUSINESS_INFO);

        List<WmPoiInfo> wmPoiInfoList = Lists.newArrayList();
        for (WmPoiBaseInfo wmPoiBaseInfo : wmPoiBaseInfoList) {
            WmPoiInfo wmPoiInfo = new WmPoiInfo();
            Integer wmPoiId = wmPoiBaseInfo.getId();

            wmPoiInfo.setWmPoiId(wmPoiId);
            wmPoiInfo.setWmPoiName(wmPoiBaseInfo.getWmPoiName());
            wmPoiInfo.setWmPoiAddress(wmPoiBaseInfo.getWmPoiAddress());
            wmPoiInfo.setWmPoiCategory("");
            wmPoiInfo.setWmPoiTel(wmPoiBaseInfo.getWmPoiPhone());
            wmPoiInfo.setWmPoiPricipal(wmPoiBaseInfo.getWmPoiPrincipal());

            List<Integer> wmPoiModuleStatusList = Lists.newArrayList();
            wmPoiModuleStatusList.add(wmPoiBaseInfoStatusMap.get(wmPoiId) != null ? wmPoiBaseInfoStatusMap.get(wmPoiId) : PoiConstant.PoiModuleStatus.STATUS_ERROR);
            wmPoiModuleStatusList.add(wmPoiQuaStatusMap.get(wmPoiId) != null ? wmPoiQuaStatusMap.get(wmPoiId) : PoiConstant.PoiModuleStatus.STATUS_ERROR);
            wmPoiModuleStatusList.add(wmPoiSettleStatusMap.get(wmPoiId) != null ? wmPoiSettleStatusMap.get(wmPoiId) : PoiConstant.PoiModuleStatus.STATUS_ERROR);
            wmPoiModuleStatusList.add(wmPoiDeliveryStatusMap.get(wmPoiId) != null ? wmPoiDeliveryStatusMap.get(wmPoiId) : PoiConstant.PoiModuleStatus.STATUS_ERROR);
            wmPoiModuleStatusList.add(wmPoiBusinessStatusMap.get(wmPoiId) != null ? wmPoiBusinessStatusMap.get(wmPoiId) : PoiConstant.PoiModuleStatus.STATUS_ERROR);
            wmPoiInfo.setWmPoiModuleStatus(wmPoiModuleStatusList);

            wmPoiBaseInfoList.add(wmPoiBaseInfo);
        }
        return new PageData.Builder<WmPoiInfo>()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalPage(pageData.getTotalPage())
                .totalSize(pageData.getTotalSize())
                .data(wmPoiInfoList)
                .build();
    }

    private Map<Integer, Integer> getWmPoiSettleStatusMap(List<Integer> wmPoiIdList) {
        List<CustomerSettlePoi> customerSettlePoiList = customerSettlePoiService.getByWmPoiIdList(wmPoiIdList);
        if (CollectionUtils.isEmpty(customerSettlePoiList)) {
            return Maps.newHashMap();
        }

        List<Integer> customerSettleIdList = Lists.transform(customerSettlePoiList, new Function<CustomerSettlePoi, Integer>() {
            @Override
            public Integer apply(CustomerSettlePoi input) {
                return input.getSettleId();
            }
        });
        List<CustomerSettle> customerSettleList = customerSettleService.getBySettleIdList(customerSettleIdList);
        Map<Integer, CustomerSettle> customerSettleMap = Maps.uniqueIndex(customerSettleList, new Function<CustomerSettle, Integer>() {
            @Override
            public Integer apply(CustomerSettle input) {
                return input.getId();
            }
        });

        Map<Integer, Integer> wmPoiSettleStatusMap = Maps.newHashMap();
        for (CustomerSettlePoi customerSettlePoi : customerSettlePoiList) {
            wmPoiSettleStatusMap.put(customerSettlePoi.getWmPoiId(), customerSettleMap.get(customerSettlePoi.getSettleId()).getStatus());
        }
        return wmPoiSettleStatusMap;
    }

    private Map<String, Map<Integer, Integer>> getWmPoiBaseMap(List<Integer> wmPoiIdList) {
        List<WmPoiBaseInfo> wmPoiBaseInfoList = wmPoiBaseInfoService.getByIdList(wmPoiIdList);
        Map<Integer, Integer> wmPoiBaseInfoStatusMap = Maps.newHashMap();
        Map<Integer, Integer> wmPoiBusinessInfoStatusMap = Maps.newHashMap();
        for (WmPoiBaseInfo wmPoiBaseInfo : wmPoiBaseInfoList) {
            wmPoiBaseInfoStatusMap.put(wmPoiBaseInfo.getId(), wmPoiBaseInfo.getStatus());
            wmPoiBusinessInfoStatusMap.put(wmPoiBaseInfo.getId(), wmPoiBaseInfo.getBusinessInfoStatus());
        }

        Map<String, Map<Integer, Integer>> map = Maps.newHashMap();
        map.put(POI_BASE_INFO, wmPoiBaseInfoStatusMap);
        map.put(POI_BUSINESS_INFO, wmPoiBusinessInfoStatusMap);
        return map;
    }

    private Map<Integer, Integer> getWmPoiDeliveryStatusMap(List<Integer> wmPoiIdList) {
        List<WmPoiDeliveryInfo> wmPoiDeliveryInfoList = wmPoiDeliveryInfoService.getByWmPoiIdList(wmPoiIdList);
        Map<Integer, Integer> wmPoiDeliveryStatusMap = Maps.newHashMap();
        for (WmPoiDeliveryInfo wmPoiDeliveryInfo : wmPoiDeliveryInfoList) {
            wmPoiDeliveryStatusMap.put(wmPoiDeliveryInfo.getWmPoiId(), wmPoiDeliveryInfo.getStatus());
        }
        return wmPoiDeliveryStatusMap;
    }

    private Map<Integer, Integer> getWmPoiQuaStatusMap(List<Integer> wmPoiIdList) {
        List<WmPoiQua> wmPoiQuaList = wmPoiQuaService.getByWmPoiIdList(wmPoiIdList);
        Map<Integer, Integer> wmPoiQuaStatusMap = Maps.newHashMap();
        for (WmPoiQua wmPoiQua : wmPoiQuaList) {
            wmPoiQuaStatusMap.put(wmPoiQua.getWmPoiId(), wmPoiQua.getStatus());
        }
        return wmPoiQuaStatusMap;
    }

    @Override
    public void distributePrincipal(Integer wmPoiId, String principalId, String opUserId) {
        wmPoiBaseInfoService.distributePrincipal(wmPoiId, principalId, opUserId);
    }
}
