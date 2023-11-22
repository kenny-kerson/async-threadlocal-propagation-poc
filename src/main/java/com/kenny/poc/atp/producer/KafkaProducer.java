package com.kenny.poc.atp.producer;

import com.kenny.poc.atp.event.CommonMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, CommonMessage> kafkaTemplate;

    public void sendMessage( final String topic, final CommonMessage payload ) {
        // TODO: 비동기처리 결과에 대한 로직 추가되어야 함
        kafkaTemplate.send(topic, payload);
    }
}
