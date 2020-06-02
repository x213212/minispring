package com.spring.demo.utils;

import com.spring.demo.anno.*;
import com.spring.demo.anno.aop.*;
import com.spring.demo.servlet.MyDispatcherServlet;
import com.spring.demo.sqlSession.MyConfiguration;
import com.spring.demo.sqlSession.MySqlsession;
import net.sf.cglib.proxy.Enhancer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;


import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;


public class UtilsScan {
    public static List<Map <String , Object>> ioclist = new ArrayList<>();
    public static List<Map <String , Object>> proxyioclist = new ArrayList<>();

    private Properties properties = new Properties();
    private static MyConfiguration myConfiguration = new MyConfiguration();
    private static MySqlsession sqlsession=new MySqlsession();
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
                if(className.equals("aop") || className.equals("run") ||   className.equals("MyDispatcherServlet") || className.equals("UtilsScan") )
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
                    else if(classtmp.getMethods().length >0){
                        Method[] methods = classtmp.getDeclaredMethods() ;
                        for( Method m : methods){
                            if(m.isAnnotationPresent(Select.class)){

                                //UserDao tmp  = (UserDao) Proxy.newProxyInstance(UserDao.class.getClassLoader(),new Class[]{UserDao.class}, handler);
                                Map<String , Object>  map= new HashMap<>();
                                //map.put(tmp.getClass().getSimpleName() , tmp);
                                //Advice2 handler2 = new BeforeAdvice3();
                               // object = classtmp.newInstance();

                                SqlInvocationHandler handler  = new SqlInvocationHandler(myConfiguration,sqlsession ,m.getAnnotation(Select.class).value());
                                object  = Proxy.newProxyInstance(classtmp.getClassLoader(),new Class[]{classtmp},handler);
                               // ((UserDao)object).query();
                                //Proxy.newProxyInstance(UserDao.class.getClassLoader(),new Class[]{UserDao.class}, handler);
                              // object =classtmp.newInstance();
//                                Object f = Enhancer.create(UserDao.class, handler2);
                                //map.put(tempClass2.getSimpleName(), f);

                                map.put(className,object);
                                ioclist.add(map);
                            }
                        }
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
//        System.out.println("ioclist hash map : "+ioclist);
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
                    Class tempClass = tempObject.getClass() ;
                    ComponentTest[] tempType = (ComponentTest[]) tempClass.getAnnotationsByType(ComponentTest.class);
                    String tempClassType=null;
                    String baseUrl ="";
                    try {
                        tempClassType = tempType[0].value()[0];

                        System.out.println(tempType[0].value()[0]);
                    } catch ( Exception e){
                        tempClass = tempObject.getClass().getSuperclass();
                        tempType = (ComponentTest[])       tempClass.getAnnotationsByType(ComponentTest.class);
                        tempClassType =tempType[0].value()[0];
                    }
                    if (tempClassType.equals("Controller")  ) {

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
                    }


                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static  List<Map <String , Object>> BeanAopMapping( List<Map <String , Object>> master) {
        List<Map<String, Object>> tmp =   new ArrayList<>() ;

        for (Map<String, Object> annotations : master) {
            for (String annotationkv : annotations.keySet()) {
                Object tempObject = annotations.get(annotationkv);
                Class tempClass = tempObject.getClass();
                ComponentTest[] tempType = (ComponentTest[]) tempClass.getAnnotationsByType(ComponentTest.class);
                String tempClassType = null;
                try {
                    tempClassType = tempType[0].value()[0];
                    System.out.println(tempType[0].value()[0]);

                } catch (Exception e) {
                    tempClassType = "";
                }

                if (tempClassType.equals("Default") || tempClassType.equals("") || tempClassType.equals("Controller")    ) {
                    Field[] fields = tempClass.getDeclaredFields();

                    if (fields.length >0  ) {
                        for (Field tempfield : fields) {
                  //  System.out.println(tempfield.getType() +"2112");

                            if (tempfield.isAnnotationPresent(Autowired.class)) {
                                String targetName = tempfield.getType().getSimpleName();


                                for (Map<String, Object> annotationchilds : ioclist) {
                                    for (String annotationchildkv : annotationchilds.keySet()) {
                                        if (annotationchilds.get(annotationchildkv).getClass().getSimpleName().equals(targetName) || annotationchildkv.equals(targetName)) {
// && 'annotationchildkv.equals(targetName)'
                                            tempfield.setAccessible(true);
                                            try {
                                                Map<String, Object> map = new HashMap<>();
                                                map.put(targetName, annotationchilds.get(targetName));
//
                                                int k =0;
                                                boolean find = false;
                                                for (Map<String, Object> proxyio : proxyioclist) {
                                                    for (String proxyiokv : proxyio.keySet()) {
                                                        if(proxyiokv.equals(targetName)){
                                                            tempfield.set(tempObject, proxyio.get(targetName));
                                                                find =true;
                                                          //  proxyioclist.remove(k);
                                                            break;
                                                        }
                                                        k++;
                                                    }

                                                }
                                                if(find == false)
                                                {
                                                    tempfield.set(tempObject, annotationchilds.get(targetName));


                                                }
                                                tmp.add(map);
                                                BeanAopMapping(tmp);

                                            } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        }

                                    }
                                }



                            }
                        }
                    } else
                        return tmp;


                }



            }
        }

        return master;
    }


    public static  List<Map <String , Object>> ScanAOP( List<Map <String , Object>> master) {
        List<Map<String, Object>> tmp =   new ArrayList<>() ;
        for (Map <String , Object>  annotations: master ){
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


                if( tempClassType.equals("Aspect")) {
                    Method[] Methods = tempClass.getMethods();
                    for (Method method : Methods) {
                        List<Map <String , Object>> methodlist =  new ArrayList<>();
                        Before[] filters = null;
                        if(method.getAnnotationsByType(Before.class).length >0){
                         filters = method.getAnnotationsByType(Before.class);

                            Advice2 AfterAdvice = null;

                            for (Before filter : filters) {
                                for (int i = 0; i < filter.value().length; i++) {
                                    System.out.println(filter.value()[i]);
                                }

                                try {
                                    for (Map<String, Object> annotations2 : ioclist) {
                                        for (String annotationkv2 : annotations2.keySet()) {
                                            Object tempObject2 = annotations2.get(annotationkv2);
                                            Class tempClass2 = tempObject2.getClass();
                                            if (tempClass2.getName().equals(filter.value()[0].toString())) {

                                                Method aopmethod = tempClass.getMethod(filter.value()[1].toString(), null);

                                                Map<String, Object> map = new HashMap<>();
                                               // Map<Object, Object> map2 = new HashMap<>();

                                                Advice2 handler = new BeforeAdvice2(tempObject2, aopmethod,null, tempObject);

                                                Object f = Enhancer.create(tempClass2, handler);
                                                //map.put(tempClass2.getSimpleName(), f);

                                                map.put(tempClass2.getSimpleName(), f);
                                                //map2.put(tempObject2, aopmethod);
                                               // methodlist.add( map2);
                                                proxyioclist.add(map);

                                                tmp.add(map);

                                               ScanAOP(tmp);
                                                break;
                                            }
                                            //ScanAOP(tmp);

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
            }


        return master;
    }
    public static  List<Map <String , Object>> AOPMapping (List<Map <String , Object>> master) {
        List<Map <String , Object>> tmp = new ArrayList<>() ;
        for( int s = 0 ; s < master.size();s ++){
            Set set =((HashMap) master.get(s)).entrySet();
            Iterator i =set.iterator();
            // Display elements
            while(i.hasNext()) {
                Map.Entry me = (Map.Entry)i.next();
                System.out.print(me.getKey() + ": ");
                if(me.getValue()!= null)
                if( me.getValue().getClass().getSuperclass().getDeclaredFields().length >0 ){
                    Map <String , Object> tst = new HashMap<>();
                    Field[] fields = me.getValue().getClass().getSuperclass().getDeclaredFields();
                    for (Field tempfield : fields) {
                        String targetName = tempfield.getType().getSimpleName();
                        for (Map<String, Object> annotationchilds : proxyioclist) {

                                    if ( annotationchilds.keySet().contains(targetName) ){
                                      tempfield.setAccessible(true);
                                        try {
                                            Map<String, Object> map = new HashMap<>();
                                            map.put( targetName, annotationchilds.get(targetName));
                                            tempfield.set(me.getValue(), annotationchilds.get(targetName));
                                            tmp.add(map);
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
        return master;
    }
    public static  void Switchbean(){

        for(int i = 0 ; i <ioclist.size() ; i++)
            for(int j = 0 ; j <proxyioclist.size() ; j++){
                if(ioclist.get(i).keySet().toString().equals(proxyioclist.get(j).keySet().toString()) ){
                    ioclist.set(i,proxyioclist.get(j));
                    break;
                }
            }
    }
    public static void checkAnnotation()  {
        //scan aop
        ScanAOP(ioclist);
        AOPMapping(proxyioclist);
        BeanAopMapping(ioclist);
        Switchbean();

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


    public static Object getProxy(Object bean, InvocationHandler advice) {
        return Proxy.newProxyInstance(UtilsScan.class.getClassLoader(),
                bean.getClass().getInterfaces(), advice);
    }
    public static Object getProxy2(Object bean, Advice advice) {
        return Proxy.newProxyInstance(UtilsScan.class.getClassLoader(),
                bean.getClass().getInterfaces(), advice);
    }


}
