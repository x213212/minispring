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
        System.out.println("ewr");
        //1.加载配置文件
//        doLoadConfig(config.getInitParameter("contextConfigLocation"));
//
//        //2.初始化所有相关联的类,扫描用户设定的包下面所有的类
//        doScanner(properties.getProperty("scanPackage"));
//
//        //3.拿到扫描到的类,通过反射机制,实例化,并且放到ioc容器中(k-v  beanName-bean) beanName默认是首字母小写
//        doInstance();
//
//        //4.初始化HandlerMapping(将url和method对应上)
//        initHandlerMapping();


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

        //获取方法的参数列表
        Class<?>[] parameterTypes = method.getParameterTypes();

        //获取请求的参数
        Map<String, String[]> parameterMap = req.getParameterMap();

        //保存参数值
        Object [] paramValues= new Object[parameterTypes.length];

        String responseString ="";
        //方法的参数列表
        for (int i = 0; i<parameterTypes.length; i++){
            //根据参数名称，做某些处理
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
            String tmp1 ="",tmp2 ="" ;
            Integer sad = 0;
            if(requestParam.equals("String")){

                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value =Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");

                    paramValues[i]=value;
                    if(sad == i){
                    tmp1=value;
                    tmp2=param.getKey();}

                    sad++;
                }
            }
            responseString+= tmp2+ ":"+ tmp1+"\n"  ;
        }
        //反射調用
        try {
            method.invoke(this.controllerMap.get(url), paramValues);
            resp.getWriter().write( "doTest method success! \n"+responseString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}