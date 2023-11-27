package com.kenny.poc.atp.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = {"defaultTopic"}, groupId = "common-message-group")
    public void subscribe(final ConsumerRecord<?, ?> record) {
        log.warn("# sub : {}", record);
    }
}
