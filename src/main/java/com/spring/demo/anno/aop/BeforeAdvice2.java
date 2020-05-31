package com.spring.demo.anno.aop;

import com.spring.demo.impl.IndexServiceimpl;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class BeforeAdvice2 implements Advice2 {

    private Object bean;
    private Object o;
    private Method methodInvocation;
    private Object original;
    public BeforeAdvice2(Object bean, Method methodInvocation, Object test ) {
        this.bean = bean;
        this.methodInvocation = methodInvocation;
        this.o = test;

    }



    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
       // System.out.println("BEFORE");
      //  methodInvocation.invoke(o,null);
        methodInvocation.invoke(this.o,null);
        Object result = method.invoke(bean, args);
       // System.out.println(method.getName());

     //   method.invoke(o, null);
     //   System.out.println("AFTER");
        return result;
    }
}
