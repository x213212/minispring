package com.spring.demo.anno.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class BeforeAdvice3 implements Advice2 {

    private Object bean;
    private Object o;
    private Method methodInvocation;
    private Method BeforeMethodInvocation;
    private Method AfterMethodInvocation;


    private Object original;
    public BeforeAdvice3(Object bean, Method BeforeMethodInvocation , Method AfterMethodInvocation, Object test ) {
        this.bean = bean;
        this.methodInvocation = methodInvocation;
        this.BeforeMethodInvocation = BeforeMethodInvocation;
        this.AfterMethodInvocation = AfterMethodInvocation;
        this.o = test;

    }

    public BeforeAdvice3 (){

    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
       // System.out.println("BEFORE");
      //  methodInvocation.invoke(o,null);
        //methodInvocation.invoke(this.o,null);
        if(BeforeMethodInvocation != null)
        BeforeMethodInvocation.invoke(this.o,null);
        //Object result = method.invoke(bean, args);
        if(AfterMethodInvocation != null)
        AfterMethodInvocation.invoke(this.o,null);
       // System.out.println(method.getName());
        System.out.println("qwe");
     //   method.invoke(o, null);
     //   System.out.println("AFTER");
        return "1111111";
    }


    @Override
    public void before() {

    }

    @Override
    public void After() {

    }
}
