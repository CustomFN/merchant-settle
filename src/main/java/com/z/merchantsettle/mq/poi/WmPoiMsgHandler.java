package com.z.merchantsettle.mq.poi;

import com.z.merchantsettle.modules.customer.service.CustomerPoiService;
import com.z.merchantsettle.modules.poi.service.WmPoiBaseInfoService;
import com.z.merchantsettle.modules.poi.service.WmPoiDeliveryInfoService;
import com.z.merchantsettle.modules.poi.service.WmPoiQuaService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WmPoiMsgHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiMsgHandler.class);

    @Autowired
    private CustomerPoiService customerPoiService;
    @Autowired
    private WmPoiBaseInfoService wmPoiBaseInfoService;
    @Autowired
    private WmPoiQuaService wmPoiQuaService;
    @Autowired
    private WmPoiDeliveryInfoService wmPoiDeliveryInfoService;

    public void handleDelete(Integer customerId) {
        LOGGER.info("WmPoiMsgHandler handleDelete customerId = {}", customerId);
        if (customerId == null || customerId <= 0) {
            return;
        }

        List<Integer> wmPoiIdList = customerPoiService.getWmPoiIdListByCustomerId(customerId);
        if (CollectionUtils.isNotEmpty(wmPoiIdList)) {
            wmPoiBaseInfoService.deleteByIdList(wmPoiIdList);
            wmPoiQuaService.deleteByWmPoiIdList(wmPoiIdList);
            wmPoiDeliveryInfoService.deleteByWmPoiIdList(wmPoiIdList);
            customerPoiService.deleteByCustomerIdOnOff(customerId);
        }
    }
}
