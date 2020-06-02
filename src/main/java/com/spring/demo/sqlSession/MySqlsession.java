package com.spring.demo.sqlSession;

import java.lang.reflect.Proxy;

public class MySqlsession {
	
	private Excutor excutor= new MyExcutor();  
	
	private MyConfiguration myConfiguration = new MyConfiguration();
	
    public <T> T selectOne(String statement, Object parameter){
        return excutor.query(statement, parameter);  
    }  


}
