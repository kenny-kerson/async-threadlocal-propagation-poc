package com.kenny.poc.atp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kenny.poc.atp.context.ContextHolder;
import com.kenny.poc.atp.event.CommonMessage;
import com.kenny.poc.atp.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncService {

    private final KafkaProducer kafkaProducer;

    @Async
    public void asyncProcess() throws JsonProcessingException {
        log.warn("# asyncProcess Start!!!");
        ContextHolder.printLog();

        kafkaProducer.sendMessage("defaultTopic", new CommonMessage("20231127"));
    }
}
