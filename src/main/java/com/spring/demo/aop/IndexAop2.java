package com.spring.demo.aop;


import com.spring.demo.anno.Before;
import com.spring.demo.anno.ComponentTest;


@ComponentTest("Aspect")
public class IndexAop2 {

    @Before({"com.spring.demo.dao.IndexDao","beforeMethod"})
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
