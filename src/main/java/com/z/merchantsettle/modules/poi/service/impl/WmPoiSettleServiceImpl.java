package com.z.merchantsettle.modules.poi.service.impl;

import com.z.merchantsettle.modules.customer.domain.bo.CustomerSettle;
import com.z.merchantsettle.modules.customer.service.CustomerSettlePoiService;
import com.z.merchantsettle.modules.customer.service.CustomerSettleService;
import com.z.merchantsettle.modules.poi.service.WmPoiSettleService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WmPoiSettleServiceImpl implements WmPoiSettleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiSettleServiceImpl.class);

    @Autowired
    private CustomerSettleService customerSettleService;
    @Autowired
    private CustomerSettlePoiService customerSettlePoiService;

    @Override
    public Object showSettleInfo(Integer wmPoiId) {
        LOGGER.info("showSettleInfo wmPoiId = {}", wmPoiId);

        List<Integer> settleIdList = customerSettlePoiService.getSettleIdByWmPoiId(wmPoiId);
        if (CollectionUtils.isEmpty(settleIdList)) {
            return null;
        }
        CustomerSettle customerSettle = customerSettleService.getCustomerSettleBySettleId(settleIdList.get(settleIdList.size() - 1), 0);
        return customerSettle;
    }
}
