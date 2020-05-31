package com.spring.demo.impl;

import com.spring.demo.anno.Autowired;
import com.spring.demo.anno.ComponentTest;
import com.spring.demo.anno.aop.HelloService;
import com.spring.demo.dao.IndexDao;
import com.spring.demo.dao.IndexDao2;
import com.spring.demo.serivce.IndexService;


@ComponentTest
public class IndexServiceimpl implements IndexService {
    @Autowired
    IndexDao dao;
    @Autowired
    IndexDao2 dao2;
    public  IndexServiceimpl(){
        System.out.println("IndexServiceimpl init" );
    }
    @Override
    public void index() {
        System.out.println("IndexServiceimpl method call" );
        dao.index();
        dao2.index();
    }
}

