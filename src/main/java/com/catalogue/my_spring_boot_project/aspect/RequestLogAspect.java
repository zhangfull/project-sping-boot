package com.catalogue.my_spring_boot_project.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.utils.ThreadLocalUtil;

@Aspect
@Component
public class RequestLogAspect {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(* com.catalogue.my_spring_boot_project.modules.*.controller..*(..))")
    public void controllerMethods() {}
    @Before("controllerMethods()")
    public void beforeRequest(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        Log.info(getClass(), "请求开始：{}", joinPoint.getSignature().toShortString());
        Log.info(getClass(), ">>> 当前线程：" + Thread.currentThread().getName());
    }

    @AfterReturning("controllerMethods()")
    public void afterRequest(JoinPoint joinPoint) {
        long cost = System.currentTimeMillis() - startTime.get();
        Log.info(getClass(), "请求结束：{}，耗时：{}ms", joinPoint.getSignature().toShortString(), cost);
        ThreadLocalUtil.remove();
        startTime.remove(); // 避免内存泄漏
    }
}

