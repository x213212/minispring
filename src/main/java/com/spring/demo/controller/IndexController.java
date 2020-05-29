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

    @MyRequestMapping("/doTest")
    public void test1(@MyRequestParam("param") String param,@MyRequestParam("param2") String param2){
        service.index();
    }
}

