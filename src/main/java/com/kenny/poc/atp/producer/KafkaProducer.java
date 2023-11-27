package com.kenny.poc.atp.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenny.poc.atp.context.Context;
import com.kenny.poc.atp.context.ContextHolder;
import com.kenny.poc.atp.event.CommonMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage( final String topic, final CommonMessage payload ) throws JsonProcessingException {
        // context 가져오기
        final Context inheritableThreadLocalContext = ContextHolder.getInheritableThreadLocalContext();

        // kafka에 전달할 record 생성 & context를 header에 넣기
        final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, objectMapper.writeValueAsString(payload));
        producerRecord.headers().add(new RecordHeader("context", objectMapper.writeValueAsString(inheritableThreadLocalContext).getBytes()));

        // kafka에 비동기로 send
        // TODO: 비동기처리 결과에 대한 로직 추가되어야 함
        kafkaTemplate.send(producerRecord);
    }
}
