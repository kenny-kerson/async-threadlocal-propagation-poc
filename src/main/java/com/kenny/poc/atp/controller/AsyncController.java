package com.kenny.poc.atp.controller;

import com.kenny.poc.atp.context.Context;
import com.kenny.poc.atp.context.ContextHolder;
import com.kenny.poc.atp.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AsyncController {

    private final AsyncService asyncService;

    @GetMapping("/async/threadlocal/{userId}/{guid}")
    public void getAsyncThreadLocal( @PathVariable final String userId, @PathVariable final String guid ) throws InterruptedException {
        log.warn("# Controller getAsyncThreadLocal() Start!!");

        try {
            // 표현영역에서 Context를 생성한다
            ContextHolder.setContext( Context.builder()
                    .userId(userId)
                    .guid(guid)
                    .build()
            );

            ContextHolder.printLog();
            asyncService.asyncProcess();

            // @Async로 실행되는 asyncProcess()가 끝나고, 메인 쓰레드의 쓰레드로컬값은 그대로 유지되는지 확인하기 위해 슬립처리함
            Thread.sleep(2000L);
            ContextHolder.printLog();

        } catch( Exception e ) {
            throw e;

        } finally {
            // 요청이 종료되면 Context를 반환한다
            ContextHolder.resetContexts();
        }

    }
}
