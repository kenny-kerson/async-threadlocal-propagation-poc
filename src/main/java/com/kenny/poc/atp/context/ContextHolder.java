package com.kenny.poc.atp.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

@Slf4j
public class ContextHolder {

    private static final ThreadLocal<Context> threadLocalContext = new NamedThreadLocal<>("Thread Local Context");
    private static final ThreadLocal<Context> inheritableThreadLocalContext = new NamedInheritableThreadLocal<>("Inheritable Thread Local Context");

    /*
     * ContextHolder의 모든 쓰레드로컬의 자원을 반환한다
     */
    public static void resetContexts() {
        threadLocalContext.remove();
        inheritableThreadLocalContext.remove();
    }

    /*
     * Context를 ThreadLocal에 set한다
     */
    public static void setContext( final Context context ) {
        threadLocalContext.set(context);
        inheritableThreadLocalContext.set(context);
    }

    /*
     * NamedThreadLocal에서 관리하는 Context를 리턴한다
     */
    public static Context getThreadLocalContext() {
        return threadLocalContext.get();
    }

    /*
     * InheritableNamedThreadlocal에서 관리되는 Context를 리턴한다
     */
    public static Context getInheritableThreadLocalContext() {
        return inheritableThreadLocalContext.get();
    }

    /*
     * ThreadLocal에 담겨있는 Context 객체를 출력한다
     */
    public static void printLog() {
        log.warn("# threadLocalContext : {}", threadLocalContext.get().toString());
        log.warn("# inheritableThreadLocalContext : {}", inheritableThreadLocalContext.get().toString());
    }
}
