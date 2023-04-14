package com.coder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

// 实现bean的注入
//@Component
// 定义切面
//@Aspect
@Deprecated
public class AlphaAspect {

    // 定义切点，service下的所有类的所有方法的所有参数所有返回值都要处理
    @Pointcut("execution(* com.coder.community.service.*.*(..))")
    public void pointcut() {

    }

    // 连接点前织入代码逻辑
    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    // 连接点后
    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    // 在有返回值以后处理
    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    // 在抛异常的时候
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    // 在连接点前后都织入逻辑
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        // 调用目标组件（原始对象）的方法
        Object obj = joinPoint.proceed();
        System.out.println("around after");
        return obj;
    }

}
