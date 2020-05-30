package com.spring.demo.utils;

import org.apache.catalina.LifecycleException;

import java.util.Map;

public class AnnotationConfigApplicationContext {
    public UtilsScan tmp = new UtilsScan();
    public  AnnotationConfigApplicationContext () throws LifecycleException {

    }
    public Object getBean (String className)
    {
        for (Map<String, Object> map : tmp.ioclist ){
            Object object = map.get(className);
            if(object!=null)
            {
                return object;
            }
        }
        return  null;
    }
}
