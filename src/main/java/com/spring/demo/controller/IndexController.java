package com.spring.demo.controller;
import com.spring.demo.anno.Autowired;
import com.spring.demo.anno.ComponentTest;
import com.spring.demo.anno.MyRequestMapping;
import com.spring.demo.anno.MyRequestParam;
import com.spring.demo.dao.IndexDao;
import com.spring.demo.impl.IndexServiceimpl;
import com.spring.demo.serivce.IndexService;
import com.spring.demo.utils.AnnotationConfigApplicationContext;
import org.apache.catalina.LifecycleException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ComponentTest("Controller")
@MyRequestMapping("/test")
public class IndexController {

    @Autowired
    IndexServiceimpl service;


    @Autowired
    IndexServiceimpl service2;

    @MyRequestMapping("/doTest")
    public String test1(@MyRequestParam("param") String param,
                      @MyRequestParam("param2") String param2){
        System.out.println("IndexController method call");
        service.index();

        return "jojo";
    }

    @MyRequestMapping("/doTest2")
    public String test2(@MyRequestParam("param") String param,
                        @MyRequestParam("param2") String param2){
        this.test1("1,2","12");

        return "jojo";
    }
}

