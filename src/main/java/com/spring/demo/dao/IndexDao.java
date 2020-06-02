package com.spring.demo.dao;

import com.spring.demo.anno.Autowired;
import com.spring.demo.anno.ComponentTest;

@ComponentTest
public class IndexDao {

    public IndexDao(){
        System.out.println("IndexDao init" );
    }
    public String  index(){
        System.out.println("IndexDao index method call");
        return "dao init";
    }
}
