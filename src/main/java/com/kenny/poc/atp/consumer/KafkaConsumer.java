package com.kenny.poc.atp.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    // TODO : payload를 String이 아니라 Object로 받도록 수정
    // TODO : consumer group으로 consumer를 엮어서 어떻게 동작하는지 확인
    // TODO : consumer 처리 오류시 몇번까지 retry 하는지, 그 이후에는 어떻게 되는지 확인
    // TODO : kafka의 현재 offset이 어떻게 되는지 확인하는 방법
    @KafkaListener(topics = {"defaultTopic"}, groupId = "common-message-group")
    public void subscribe(final ConsumerRecord<String, String> record) {
        log.warn("# sub : {}", record);
    }
}
