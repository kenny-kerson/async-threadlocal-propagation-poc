package com.kenny.poc.atp.service;

import com.kenny.poc.atp.context.ContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncService {

    @Async
    public void asyncProcess() {
        log.warn("# asyncProcess Start!!!");

        ContextHolder.printLog();
    }
}
