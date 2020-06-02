package com.spring.demo.impl;

import com.spring.demo.anno.Autowired;
import com.spring.demo.anno.ComponentTest;
import com.spring.demo.dao.IndexDao;
import com.spring.demo.dao.UserDao;
import com.spring.demo.entity.User;
import com.spring.demo.serivce.IndexService;

import javax.jws.soap.SOAPBinding;


@ComponentTest
public class IndexServiceimpl implements IndexService {
    @Autowired
    IndexDao dao;

    @Autowired
    UserDao dao2;
//    @Autowired
//    IndexDao2 dao2;
    public  IndexServiceimpl(){
        System.out.println("IndexServiceimpl init" );
    }
    @Override
    public User index() {
        System.out.println("IndexServiceimpl method call" );

       // System.out.println(  dao2.getUserById("1"));
        return  dao2.getUserById("1");

       // dao2.index();
    }
}

