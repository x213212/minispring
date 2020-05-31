package com.spring.demo.dao;

import com.spring.demo.anno.ComponentTest;

@ComponentTest
public class IndexDao2 {
    public IndexDao2(){
        System.out.println("IndexDao2 init" );
    }
    public String  index(){
        System.out.println("IndexDao2 index method call");
        return "dao2 init";
    }
}
