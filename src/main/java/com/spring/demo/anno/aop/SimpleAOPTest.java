package com.spring.demo.anno.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SimpleAOPTest {
    public static void main(String[] args) {
//        Runnable runnbale = new Runnable() {
//            public void run() {
//                System.out.println("run me!");
//            }
//        };
//        這樣的使用方式，往往會讓Java程式拉的很長，變得十分複雜。因此Lambda最重要的功能，就是取代Functional Interface所產出的匿名類別，簡化程式碼，甚至還可以提升效能(稍候提到)。舉例，同樣的Runnable使用方式，可以減化成：
//
//        Runnable runnbale = () -> System.out.println("run me!");
        MethodInvocation logTask = () -> System.out.println("run me!");
        MethodInvocation2 tmp = new MethodInvocation2();
        Class c = MethodInvocation2.class;
        HelloServiceImpl helloService = new HelloServiceImpl();
        Constructor con = c.getDeclaredConstructors()[0];
        Advice beforeAdvice = null;
        try {
            Object o = con.newInstance();
            Method method =  c.getMethod("invoke",null);
            beforeAdvice = new BeforeAdvice(helloService,  method,o);


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        // 2.创建一个Advice
        //Advice beforeAdvice = new BeforeAdvice(helloService, (Method)tmp);


        // 3.为目标对象生成代理
        HelloService helloServiceProxy = (HelloService) SimpleAOP.getProxy(helloService, beforeAdvice);


        helloServiceProxy.sayHelloWorld();
    }
}
