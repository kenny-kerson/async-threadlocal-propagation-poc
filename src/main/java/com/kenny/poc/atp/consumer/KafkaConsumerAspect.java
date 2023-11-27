package com.kenny.poc.atp.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenny.poc.atp.context.Context;
import com.kenny.poc.atp.context.ContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Joinpoint;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerAspect {

    private final ObjectMapper objectMapper;

    @Before("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void beforeKafkaConsumer(Joinpoint jp, ConsumerRecord<?, ?> record) throws JsonProcessingException {
        final String contextString = new String(record.headers().lastHeader("context").value());
        log.warn("# beforeKafkaConsumer : {}", contextString);

        final Context context = objectMapper.readValue(contextString, Context.class);

        ContextHolder.setContext(context);
        ContextHolder.printLog();
    }

    @After("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void afterKafkaConsumer() {
        log.warn("# afterKafkaConsumer : 자원을 정리한다" );
        ContextHolder.resetContexts();
    }

    @AfterThrowing("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void afterThrowingKafkaConsumer() {
        log.warn("# afterThrowingKafkaConsumer : 자원을 정리한다" );
        ContextHolder.resetContexts();
    }
}
