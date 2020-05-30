package com.spring.demo.servlet;

import com.spring.demo.anno.ComponentTest;
import com.spring.demo.anno.MyRequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;


public class MyDispatcherServlet extends HttpServlet{

    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new  HashMap<>();

    private Map<String, Object> controllerMap  =new HashMap<>();

    public MyDispatcherServlet (Map<String, Method> tmp ,Map<String, Object>  tmp2){
        this.handlerMapping = tmp;
        this.controllerMap = tmp2;

    }
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("MyDispatcherServlet init");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // 統一攔截請求
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //統一攔截請求
            doDispatch(req,resp);
        } catch (Exception e) {
            resp.getWriter().write("500!! Server Exception");
        }

    }



    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if(handlerMapping.isEmpty()){
            return;
        }

        String url =req.getRequestURI();
        String contextPath = req.getContextPath();

        url=url.replace(contextPath, "").replaceAll("/+", "/");

        if(!this.handlerMapping.containsKey(url)){
            resp.getWriter().write("404 NOT FOUND!");
            return;
        }

        Method method =this.handlerMapping.get(url);

        //取得參數所有型態
        Class<?>[] parameterTypes = method.getParameterTypes();

        //取得請求參數
        Map<String, String[]> parameterMap = req.getParameterMap();

        //儲存請求參數
        Object [] paramValues= new Object[parameterTypes.length];

        Map<String, String> params = new  HashMap<>();
        //List keys = new ArrayList(parameterMap.keySet());
        String responseString ="";
        
        //方法的参数列表
        for (int i = 0; i<parameterTypes.length; i++){
            //處理參數
            String requestParam = parameterTypes[i].getSimpleName();


            if (requestParam.equals("HttpServletRequest")){
                //對參數做處理
                paramValues[i]=req;
                continue;
            }
            if (requestParam.equals("HttpServletResponse")){
                paramValues[i]=resp;
                continue;
            }


            if(requestParam.equals("String")){



                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {

                    String value =Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    params.put(param.getKey(),value);

                }
            }


        }
        for (Map.Entry<String, String> param : params.entrySet()) {
            responseString+= param.getKey() +":" +param.getValue()  + "\n";

        }
        //反射調用
        try {
           Object tmp = method.invoke(this.controllerMap.get(url), paramValues);

            resp.getWriter().write( "doTest method success! \n"+responseString
                    +"return type :"+method.getReturnType() + ": value :" + (String)tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}