package com.z.merchantsettle.mq.customer;

import com.z.merchantsettle.modules.customer.service.CustomerContractService;
import com.z.merchantsettle.modules.customer.service.CustomerKpService;
import com.z.merchantsettle.modules.customer.service.CustomerSettleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerMsgHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMsgHandler.class);

    @Autowired
    private CustomerKpService customerKpService;
    @Autowired
    private CustomerContractService customerContractService;
    @Autowired
    private CustomerSettleService customerSettleService;

    public void handleDelete(Integer customerId) {
        LOGGER.info("CustomerMsgHandler handleDelete customerId = {}", customerId);
        if (customerId == null || customerId <= 0) {
            return;
        }

        customerKpService.deleteByCustomerId(customerId, "系统()");
        customerContractService.deleteByCustomerId(customerId, "系统()");
        customerSettleService.deleteByCustomerId(customerId, "系统()");
    }

}
