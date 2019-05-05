package com.z.merchantsettle.mq.poi;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.mq.MsgOpType;
import com.z.merchantsettle.mq.customer.CustomerMsg;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class WmPoiListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmPoiListener.class);

    private static final String CUSTOMER_SETTLE_TOPIC = "customer_settle_topic";

    @Autowired
    private WmPoiMsgHandler wmPoiMsgHandler;

//    @KafkaListener(topics = {CUSTOMER_SETTLE_TOPIC})
    public void listen(ConsumerRecord<String, String> record) {
        String recordData = record.value();
        LOGGER.info("WmPoiListener listen recordData = {}", JSON.toJSONString(recordData));
        if (StringUtils.isNotBlank(recordData)) {
            CustomerMsg customerMsg = JSON.parseObject(recordData, CustomerMsg.class);

            if (customerMsg.getCustomerId() != null && MsgOpType.DELETE == customerMsg.getType()) {
                wmPoiMsgHandler.handleDelete(customerMsg.getCustomerId());
            }
        }
    }
}
