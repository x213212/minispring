package com.spring.demo.sqlSession;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 读取与解析配置信息，并返回处理后的Environment
 */
public class MyConfiguration {
	private static ClassLoader loader = ClassLoader.getSystemClassLoader();

	/**
	 * 读取xml信息并处理
	 */
	public Connection build(String resource){
	    try {
	        InputStream stream = loader.getResourceAsStream(resource);
			SAXReader reader = new SAXReader();
			Document document = reader.read(stream);
			Element root = document.getRootElement();
			return evalDataSource(root);
		} catch (Exception e) {
			throw new RuntimeException("error occured while evaling xml " + resource);
		}
	}
	
	private Connection evalDataSource(Element node) throws ClassNotFoundException {
        if (!node.getName().equals("database")) {
        	throw new RuntimeException("root should be <database>");
        }
		String driverClassName = null;
		String url = null;
		String username = null;
		String password = null;
		//获取属性节点
		for (Object item : node.elements("property")) {
			Element i = (Element) item;			
			String value = getValue(i);
			String name = i.attributeValue("name");
			if (name == null || value == null) {
				throw new RuntimeException("[database]: <property> should contain name and value");
			}
			//赋值
			switch (name) {
				case "url" : url = value; break;
				case "username" : username = value; break;
				case "password" : password = value; break;
				case "driverClassName" : driverClassName = value; break; 
				default : throw new RuntimeException("[database]: <property> unknown name");
			}
		}
		
		 Class.forName(driverClassName);
		 Connection connection = null;
		try {
			//建立数据库链接
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	//获取property属性的值,如果有value值,则读取 没有设置value,则读取内容
	private String getValue(Element node) {
		return node.hasContent() ? node.getText() : node.attributeValue("value");
	}
	
	


}
