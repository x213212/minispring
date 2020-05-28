package com.spring.demo.anno.aop;

import java.lang.reflect.Method;

public class BeforeAdvice implements Advice {

    private Object bean;
    private Object o;
    private Method methodInvocation;
    public BeforeAdvice(Object bean, Method methodInvocation,Object test) {
        this.bean = bean;
        this.methodInvocation = methodInvocation;
        this.o = test;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 在目标方法执行前调用通知
        //methodInvocation.invoke(o,null);
        methodInvocation.invoke(o,null);

        Object result = method.invoke(bean, args);



        return result;
    }
}
