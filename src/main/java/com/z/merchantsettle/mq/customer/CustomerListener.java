package com.z.merchantsettle.mq.customer;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.mq.MsgOpType;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class CustomerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerListener.class);

    @Autowired
    private CustomerMsgHandler customerMsgHandler;

    private static final String CUSTOMER_TOPIC = "customer_topic";

//    @KafkaListener(topics = {CUSTOMER_TOPIC})
    public void listen(ConsumerRecord<String, String> record) {
        String recordData = record.value();
        LOGGER.info("CustomerListener listen recordData = {}", JSON.toJSONString(recordData));
        if (StringUtils.isNotBlank(recordData)) {
            CustomerMsg customerMsg = JSON.parseObject(recordData, CustomerMsg.class);

            if (customerMsg.getCustomerId() != null && MsgOpType.DELETE == customerMsg.getType()) {
                customerMsgHandler.handleDelete(customerMsg.getCustomerId());
            }
        }
    }
}
