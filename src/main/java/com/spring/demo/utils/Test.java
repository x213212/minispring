package com.spring.demo.utils;


import com.spring.demo.serivce.IndexService;

import java.lang.reflect.InvocationTargetException;

public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext ();
        IndexService service = (IndexService) annotationConfigApplicationContext.getBean("IndexServiceimpl");
        for(int i = 0 ; i < 10 ; i ++)
        {

                service.index();


        }
        //System.out.println(service);
    }
}
