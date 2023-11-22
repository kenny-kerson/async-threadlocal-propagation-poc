package com.kenny.poc.atp.consumer;

import com.kenny.poc.atp.event.CommonMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = {"defaultTopic"}, groupId = "common-message-group")
    public void subscribe(final CommonMessage commonMessage ) {
        log.warn("# sub : {}", commonMessage);
    }
}
