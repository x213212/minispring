package com.spring.demo.aop;


import com.spring.demo.anno.After;
import com.spring.demo.anno.Before;
import com.spring.demo.anno.ComponentTest;
import com.spring.demo.serivce.IndexService;


@ComponentTest("Aspect")
public class IndexAop {

    @Before({"com.spring.demo.impl.IndexServiceimpl","beforeMethod"})
    public void beforeMethod(){
        System.out.println("bftest");
    }
//    @After({"com.spring.demo.impl.IndexServiceimpl","AfterMethod"})
//    public void AfterMethod(){
//        System.out.println("qwd2");
//    }
//    public void test (){
//        System.out.println("hahaha");
//    }
}
