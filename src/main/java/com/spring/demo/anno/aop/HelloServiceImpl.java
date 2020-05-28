package com.spring.demo.anno.aop;

public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHelloWorld() {
        System.out.println("hello world!");
    }
}
