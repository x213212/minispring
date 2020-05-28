package com.spring.demo.anno.aop;

import java.lang.reflect.Method;

public class AfterAdvice implements Advice {

    private Object bean;
    private Object o;
    private Method methodInvocation;
    public AfterAdvice(Object bean, Method methodInvocation,Object test) {
        this.bean = bean;
        this.methodInvocation = methodInvocation;
        this.o = test;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 在目标方法执行前调用通知

        Object result = method.invoke(bean, args);
        methodInvocation.invoke(o,null);


        return result;
    }
}
