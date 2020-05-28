package com.spring.demo.utils;

import java.util.Map;

public class AnnotationConfigApplicationContext {
    public  AnnotationConfigApplicationContext (){
        UtilsScan.doScan();
    }
    public Object getBean (String className)
    {
        for (Map<String, Object> map : UtilsScan.ioclist ){
            Object object = map.get(className);
            if(object!=null)
            {
                return object;
            }
        }
        return  null;
    }
}
