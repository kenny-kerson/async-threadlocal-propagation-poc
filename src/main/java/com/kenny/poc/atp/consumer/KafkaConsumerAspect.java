package com.kenny.poc.atp.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenny.poc.atp.context.Context;
import com.kenny.poc.atp.context.ContextHolder;
import com.kenny.poc.atp.event.CommonMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Joinpoint;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerAspect {

    private final ObjectMapper objectMapper;

    // @Before에서 args를 기술하지 않으면 인스펙션 waring 발생
    // args까지 정확히 기술해야 AOP가 적용되는 메서드 시그니처를 정확히 찾을 수 있음
    @Before("@annotation(org.springframework.kafka.annotation.KafkaListener) && args(record,..)")
    public void beforeKafkaConsumer(final ConsumerRecord<String, CommonMessage> record) throws JsonProcessingException {
        final String contextString = new String(record.headers().lastHeader("context").value());
        log.warn("# beforeKafkaConsumer : {}", contextString);

        final Context context = objectMapper.readValue(contextString, Context.class);

        ContextHolder.setContext(context);
        ContextHolder.printLog("beforeKafkaConsumer");
    }

    @After("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void afterKafkaConsumer() {
        log.warn("# afterKafkaConsumer : 자원을 정리한다" );
        ContextHolder.resetContexts();
    }
}
