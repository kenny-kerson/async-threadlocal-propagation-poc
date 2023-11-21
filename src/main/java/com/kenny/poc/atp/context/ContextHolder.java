package com.kenny.poc.atp.context;

import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

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
        inheritableThreadLocalContext.set(context);ˆ
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

}
