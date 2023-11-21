package com.kenny.poc.atp.config;

import com.kenny.poc.atp.context.Context;
import com.kenny.poc.atp.context.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;

@Slf4j
public class ContextPropagationDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(final Runnable runnable) {
        final Context threadLocalContext = ContextHolder.getThreadLocalContext();
        log.warn("# decorate threadLocalContext : {}", threadLocalContext);
        return () -> {
                try {
                    // 비동기 쓰레드를 시작하기전에 ThreadLocal값을 전파(복사) 한다
                    ContextHolder.setThreadLocalContext(threadLocalContext);
                    log.warn("# decorate runnable threadLocalContext : {}", ContextHolder.getThreadLocalContext());

                    // 비동기 쓰레드 시작
                    runnable.run();

                } finally {
                    // 비동기 쓰레드 종료 후 자원정리
                    ContextHolder.resetContexts();
                }
        };
    }
}
