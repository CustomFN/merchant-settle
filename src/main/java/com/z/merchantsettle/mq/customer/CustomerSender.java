package com.z.merchantsettle.mq.customer;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerSender.class);

//    @Autowired
//    private KafkaTemplate kafkaTemplate;

    public void send(String topic, Integer customerId, Integer type) {
        CustomerMsg msg = new CustomerMsg();
        msg.setCustomerId(customerId);
        msg.setType(type);

        LOGGER.info("topic = {}, CustomerSender send msg = {}", topic, JSON.toJSONString(msg));
//        kafkaTemplate.send(topic, JSON.toJSONString(msg));
    }

    public void send(String topic, CustomerMsg customerMsg) {
        LOGGER.info("topic = {}, CustomerSender send msg = {}", topic, JSON.toJSONString(customerMsg));
//        kafkaTemplate.send(topic, JSON.toJSONString(customerMsg));
    }
}
