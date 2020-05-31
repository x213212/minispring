package com.spring.demo.impl;

import com.spring.demo.anno.Autowired;
import com.spring.demo.anno.ComponentTest;
import com.spring.demo.dao.IndexDao;
import com.spring.demo.serivce.IndexService;


@ComponentTest
public class IndexServiceimpl2 implements IndexService {
    @Autowired
    IndexDao dao;

    public IndexServiceimpl2(){
        System.out.println("IndexServiceimpl init" );
    }
    @Override
    public void index() {
        System.out.println("IndexServiceimpl method call" );
        dao.index();

    }
}

