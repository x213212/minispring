package com.spring.demo.anno.aop;


import com.spring.demo.entity.User;

import com.spring.demo.sqlSession.Excutor;
import com.spring.demo.sqlSession.MyConfiguration;
import com.spring.demo.sqlSession.MyExcutor;
import com.spring.demo.sqlSession.MySqlsession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class SqlInvocationHandler implements InvocationHandler {

    private Object bean;
    private Object o;
    private Method methodInvocation;
    private MySqlsession mySqlsession;

    private MyConfiguration myConfiguration;
    private String sql;
    public SqlInvocationHandler(MyConfiguration myConfiguration, MySqlsession mySqlsession , String sql) {
        this.myConfiguration=myConfiguration;
        this.mySqlsession=mySqlsession;
        this.sql = sql;
    }

    public SqlInvocationHandler(Object bean, Method methodInvocation, Object test) {
        this.bean = bean;
        this.methodInvocation = methodInvocation;
        this.o = test;
    }

    public SqlInvocationHandler() {

    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 在目标方法执行前调用通知
        System.out.println( sql);
        //Object result = method.invoke(proxy);
        //methodInvocation.invoke(o,null);

//        //是否是xml文件对应的接口
//        if(!method.getDeclaringClass().getName().equals(){
//            return null;
//        }
        //mySqlsession.selectOne(sql, String.valueOf(args[0]));
//        List<Function> list = readMapper.getList();
//        if(null != list || 0 != list.size()){
//            for (Function function : list) {
//                //id是否和接口方法名一样
//                if(method.getName().equals(function.getFuncName())){
//                    return mySqlsession.selectOne(function.getSql(), String.valueOf(args[0]));
//                }
//            }
//        }

//           mySqlsession.selectOne(sql, String.valueOf(args[0]));
//        MySqlsession sqlsession=new MySqlsession();
//        UserMapper mapper = sqlsession.getMapper(UserMapper.class);
//        User user =  mySqlsession.selectOne(sql, String.valueOf(args[0]));
//        System.out.println(user);
        //UserMapper mapper = mySqlsession.selectOne(sql, String.valueOf(args[0]));
        return  mySqlsession.selectOne(sql, String.valueOf(args[0]));
    }
}
