package com.spring.demo.utils;

import com.spring.demo.anno.aop.SqlInvocationHandler;
import com.spring.demo.dao.UserDao;

import java.lang.reflect.Proxy;

public class testdao {
    public static void main(String[] args) {
        SqlInvocationHandler handler = new SqlInvocationHandler();
        UserDao tmp  = (UserDao) Proxy.newProxyInstance(UserDao.class.getClassLoader(),new Class[]{UserDao.class}, handler);
        tmp.getUserById("1");
    }
}
