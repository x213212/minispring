package com.spring.demo.utils;

import com.spring.demo.anno.*;
import com.spring.demo.anno.aop.*;
import com.spring.demo.servlet.MyDispatcherServlet;
import net.sf.cglib.proxy.Enhancer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;


import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;


public class UtilsScan {
    public static List<Map <String , Object>> ioclist = new ArrayList<>();
    public static List<Map <String , Object>> proxyioclist = new ArrayList<>();

    private Properties properties = new Properties();

    private static Map<String, Method> handlerMapping = new  HashMap<>();

    private static Map<String, Object> controllerMap  =new HashMap<>();
    public UtilsScan() throws LifecycleException {
        UtilsScan.doScan();
        java.io.File file = new java.io.File("." );
        int port = 8080;
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        tomcat.setPort(port);

        String contextPath = "/";
        String docBase = file.getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);

        HttpServlet servlet = new MyDispatcherServlet(handlerMapping,controllerMap);
        String servletName = "MyDispatcherServlet";
        String urlPattern = "/*";

        tomcat.addServlet(contextPath, servletName, servlet);
        context.addServletMappingDecoded(urlPattern, servletName);

        tomcat.start();
        tomcat.getServer().await();

//        ServletConfig tmp =this.getServletConfig();
//        doLoadConfig(tmp.getInitParameter("contextConfigLocation"));
//
//        UtilsScan.doScan();
    }
    public  static void doScan(){
        String packagePath =  "D:\\Programming\\spring\\minispringcore\\minispring\\src\\main\\java\\com\\spring\\demo";
        File file = new File (packagePath );
        String[] childFile = file.list();
        for (String fileName : childFile) {
            System.out.println(fileName);
            File childfiletmp = new File( packagePath +"\\" +fileName);
            String classFileName[] = childfiletmp.list();
            for (String className : classFileName ){
                if(className.equals("aop") || className.equals("run") ||   className.equals("MyDispatcherServlet") )
                    continue;
                className = className.substring(0,className.indexOf("."));
                Object object = null;
                try {
                    System.out.println("com.spring." + fileName +"." + className);
                    Class classtmp  =  Class.forName("com.spring.demo." + fileName +"." + className);
                    if(classtmp.isAnnotationPresent(ComponentTest.class))
                    {
                        object = classtmp.newInstance();
                        Map<String , Object>  map= new HashMap<>();
                        map.put(classtmp.getSimpleName() , object);


                        ioclist.add(map);
                        // System.out.println( "ADD ComponentTest 註解 :"+object.getClass().getSimpleName()+"object :" +object);
                    }

                } catch(InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
        System.out.println("ioclist hash map : "+ioclist);
        /*
            Autowired
         */

        checkAnnotation();
        initHandlerMapping();


    }


    private void  doLoadConfig(String location){
        //把web.xml中的contextConfigLocation对应value值的文件加载到流里面
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(location);
        try {
            //用Properties文件加载文件里的内容
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关流
            if(null!=resourceAsStream){
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private static void initHandlerMapping(){
        if(ioclist.isEmpty()){
            return;
        }
        try {
            for (Map <String , Object>  annotations: ioclist ){
                for ( String annotationkv : annotations.keySet()){
                    Object tempObject =   annotations.get(annotationkv);
                    Class tempClass = tempObject.getClass();
                    ComponentTest[] tempType = (ComponentTest[]) tempClass.getAnnotationsByType(ComponentTest.class);
                    String tempClassType=null;
                    String baseUrl ="";
                    try {
                        tempClassType = tempType[0].value()[0];

                        System.out.println(tempType[0].value()[0]);
                    } catch ( Exception e){
                        tempClassType ="";
                    }
                    if (tempClassType.equals("Controller") ) {

                        MyRequestMapping[] tempType2 = (MyRequestMapping[])  tempClass.getAnnotationsByType(MyRequestMapping.class);
                        baseUrl = tempType2[0].value();

                        Method[] methods = tempClass.getMethods();
                        for (Method method : methods) {
                            if(!method.isAnnotationPresent(MyRequestMapping.class)){
                                continue;
                            }
                            MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
                            String url = annotation.value();

                            url =(baseUrl+"/"+url).replaceAll("/+", "/");
                            handlerMapping.put(url,method);
                            controllerMap.put(url,tempObject);
                            System.out.println(url+","+method);
                        }
//                        if(tempClass.isAnnotationPresent(MyRequestMapping.class)){
//                            MyRequestMapping annotation = tempClass.getAnnotation(MyRequestMapping.class);
//                            baseUrl=annotation.value();
//                        }


                    }


                }
            }
//            for (Map.Entry<String, Object> entry: ioclist.entrySet()) {
//                Class<? extends Object> clazz = entry.getValue().getClass();
//                if(!clazz.isAnnotationPresent(ComponentTest.class)){
//                    continue;
//                }
//
//                //拼url时,是controller头的url拼上方法上的url
//                String baseUrl ="";
//                if(clazz.isAnnotationPresent(MyRequestMapping.class)){
//                    MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
//                    baseUrl=annotation.value();
//                }
//                Method[] methods = clazz.getMethods();
//                for (Method method : methods) {
//                    if(!method.isAnnotationPresent(MyRequestMapping.class)){
//                        continue;
//                    }
//                    MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
//                    String url = annotation.value();
//
//                    url =(baseUrl+"/"+url).replaceAll("/+", "/");
//                    handlerMapping.put(url,method);
//                    controllerMap.put(url,clazz.newInstance());
//                    System.out.println(url+","+method);
//                }
//
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void checkAnnotation()  {

        //Autowired
        for (Map <String , Object>  annotations: ioclist ){
            for ( String annotationkv : annotations.keySet()){
                Object tempObject =   annotations.get(annotationkv);
                Class tempClass = tempObject.getClass();
                ComponentTest[] tempType = (ComponentTest[]) tempClass.getAnnotationsByType(ComponentTest.class);
                String tempClassType=null;
                try {
                    tempClassType = tempType[0].value()[0];
                    System.out.println(tempType[0].value()[0]);

                } catch ( Exception e){
                    tempClassType ="";
                }

                if (tempClassType.equals("Default") || tempClassType.equals("") || tempClassType.equals("Controller") ) {
                    Field[] fields = tempClass.getDeclaredFields();
                    for (Field tempfield : fields) {
//                    System.out.println(tempfield.value());
                        if (tempfield.isAnnotationPresent(Autowired.class)) {
                            String targetName = tempfield.getType().getSimpleName();

                            for (Map<String, Object> annotationchilds : ioclist) {
                                for (String annotationchildkv : annotationchilds.keySet()) {
                                    if (annotationchilds.get(annotationchildkv).getClass().getSimpleName().equals(targetName)){
// && 'annotationchildkv.equals(targetName)'
                                        tempfield.setAccessible(true);
                                        try {

                                                tempfield.set(tempObject, annotationchilds.get(targetName));
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        //aop
        for (Map <String , Object>  annotations: ioclist ){
            for ( String annotationkv : annotations.keySet()){
                Object tempObject =   annotations.get(annotationkv);
                Class tempClass = tempObject.getClass();
                ComponentTest[] tempType = (ComponentTest[]) tempClass.getAnnotationsByType(ComponentTest.class);
                String tempClassType=null;
                try {
                    tempClassType = tempType[0].value()[0];
                    System.out.println(tempType[0].value()[0]);

                } catch ( Exception e){
                    tempClassType ="";
                }


                if(tempClassType.equals("Aspect")   ) {
                    Method[] Methods = tempClass.getMethods();
                    for (Method method : Methods)
                    {
                        Before[] filters = method.getAnnotationsByType(Before.class);
                        Annotation[][] tttt= method.getParameterAnnotations();

                        Advice2 AfterAdvice = null;
                        for(Before filter : filters) {
                            for (int i = 0 ; i <filter.value().length ; i ++){
                                System.out.println(filter.value()[i]);
                            }
                            Map<String , Object>  map= new HashMap<>();
                            try {
                                for (Map <String , Object>  annotations2: ioclist ){
                                    for ( String annotationkv2 : annotations2.keySet()){
                                        Object tempObject2 =   annotations2.get(annotationkv2);
                                        Class tempClass2 = tempObject2.getClass();
                                        if(tempClass2.getName().equals(filter.value()[0].toString())){
                                            //System.out.println(filter.value()[1].toString());

//                                            CglibProxyDemo.Original original = new CglibProxyDemo.Original();
//                                            AfterAdvice = new BeforeAdvice2(tempObject2,  aopmethod,tempObject);
//                                            Object f =  Enhancer.create(tempClass2,AfterAdvice);

                                            Method aopmethod =  tempClass.getMethod(filter.value()[1].toString(),null);
                                           // AfterAdvice = new BeforeAdvice2(tempObject2,  aopmethod,tempObject);
                                            //Object helloServiceProxy = UtilsScan.getProxy(tempObject2, AfterAdvice);
                                          //  AfterAdvice = new BeforeAdvice2(tempObject2,  aopmethod,tempObject);
                                           // Object helloServiceProxy2 =  Enhancer.create(tempClass2,AfterAdvice);

                                            //设置回调接口,这里的MethodInterceptor实现类回调接口，而我们又实现了MethodInterceptor,其实
                                            //这里的回调接口就是本类对象,调用的方法其实就是intercept()方法
//                                            IndexServiceimpl original = new IndexServiceimpl();
                                            Advice2 handler = new BeforeAdvice2(tempObject2,  aopmethod,tempObject);
                                            Object f =  Enhancer.create(tempClass2,handler);
                                            map.put(tempClass2.getSimpleName() , f);
//                                            IndexServiceimpl tmp = (IndexServiceimpl)f;
//                                            tmp.index();
                                            //ioclist.remove(tempObject2);
                                            for (int x =0 ; x < ioclist.size() ; x ++)
                                            {
                                                if(ioclist.get(x).keySet().equals( annotations2.keySet()))
                                                {
                                                    //System.out.println(map);

                                                    ioclist.set(x,map);
                                                    //System.out.println("ioclist hash map : "+ioclist);
                                                    break;
                                                }


                                            }

                                        }
                                        break;
                                    }
                                }

                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }

            }
        }
        //checkporxyageain
        for (Map <String , Object>  annotations: ioclist ){
            for ( String annotationkv : annotations.keySet()){
                Object tempObject =   annotations.get(annotationkv);
                Class tempClass = tempObject.getClass();
                ComponentTest[] tempType = (ComponentTest[]) tempClass.getAnnotationsByType(ComponentTest.class);
                String tempClassType=null;
                try {
                    tempClassType = tempType[0].value()[0];
                    System.out.println(tempType[0].value()[0]);

                } catch ( Exception e){
                    tempClassType ="";
                }

                if (tempClassType.equals("Default") || tempClassType.equals("") || tempClassType.equals("Controller") ) {
                    Field[] fields = tempClass.getDeclaredFields();
                    for (Field tempfield : fields) {
//                    System.out.println(tempfield.value());
                        if (tempfield.isAnnotationPresent(Autowired.class)) {
                            String targetName = tempfield.getType().getSimpleName();
                                   for(int i =0 ; i <ioclist.size();i++)
                                {

                                     if (ioclist.get(i).containsKey(targetName) ){
// && 'annotationchildkv.equals(targetName)'
                                        tempfield.setAccessible(true);
                                        try {
                                            Map<String, Object> annotationchilds = ioclist.get(i);
                                            tempfield.set(tempObject, annotationchilds.get(targetName));
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
//                            for (Map<String, Object> annotationchilds : ioclist) {
//                                for (String annotationchildkv : annotationchilds.keySet()) {
////                                    tempfield.setAccessible(true);
////                                    try {
////
////                                        tempfield.set(tempObject, annotationchilds.get(annotationchildkv));
////                                    } catch (IllegalAccessException e) {
////                                        e.printStackTrace();
////                                    }
////                                    try {
////                                        if(targetName.equals("IndexServiceimpl")){
//////                                                try {
////////                                                    Class classtmp  =  Class.forName("com.spring.demo.impl.IndexServiceimpl" );
////////                                                    Object tmp   = classtmp.cast(ioclist.get(3).get("IndexServiceimpl")) ;
//////                                                    //tmp.index();;
//////                                                    tempfield.set(tempObject,ioclist.get(3).get("IndexServiceimpl") );
//////                                                } catch (ClassNotFoundException e) {
//////                                                    e.printStackTrace();
//////                                                }
////
////                                            tempfield.set(tempObject,ioclist.get(3).get("IndexServiceimpl") );
////                                        }
////                                        else
////                                            tempfield.set(tempObject, annotationchilds.get(targetName));
////                                    } catch (IllegalAccessException e) {
////                                        e.printStackTrace();
////                                    }
////                                    break;
//                                    if (annotationchilds.get(annotationchildkv).getClass().getSimpleName().equals(targetName) ){
//// && 'annotationchildkv.equals(targetName)'
//                                        tempfield.setAccessible(true);
//                                    try {
//
//                                        tempfield.set(tempObject, annotationchilds.get(annotationchildkv));
//                                    } catch (IllegalAccessException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    }
//
//                                }
//                            }
                        }
                    }
                }

            }
        }

    }




    /**
     * 把字符串的首字母小写
     * @param name
     * @return
     */
    private String toLowerFirstWord(String name){
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }


    public static Object getProxy(Object bean, Advice advice) {
        return Proxy.newProxyInstance(UtilsScan.class.getClassLoader(),
                bean.getClass().getInterfaces(), advice);
    }
    public static Object getProxy2(Object bean, Advice advice) {
        return Proxy.newProxyInstance(UtilsScan.class.getClassLoader(),
                bean.getClass().getInterfaces(), advice);
    }

//    public static void main(String[] args) {
//        doLoadConfig(config.getInitParameter("contextConfigLocation"));
//        UtilsScan.doScan();
//    }

}
