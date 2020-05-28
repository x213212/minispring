package com.spring.demo.utils;

import com.spring.demo.anno.*;
import com.spring.demo.anno.aop.*;
import com.spring.demo.aop.IndexAop;
import com.spring.demo.serivce.IndexService;
import com.sun.net.httpserver.HttpServer;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.*;


public class UtilsScan  extends HttpServlet {
    public static List<Map <String , Object>> ioclist = new ArrayList<>();
    public static List<Map <String , Object>> proxyioclist = new ArrayList<>();

    private Map<String, Method> handlerMapping = new  HashMap<>();

    private Map<String, Object> controllerMap  =new HashMap<>();

    public  static void doScan(){
        String packagePath =  "D:\\Programming\\spring\\simplespringioc\\src\\main\\java\\com\\spring\\demo";
        File file = new File (packagePath );
        String[] childFile = file.list();
        for (String fileName : childFile) {
            System.out.println(fileName);
            File childfiletmp = new File( packagePath +"\\" +fileName);
            String classFileName[] = childfiletmp.list();
            for (String className : classFileName ){
                if(className.equals("aop")   )
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
                        Field[] fields = tempClass.getDeclaredFields();
                        for (Field tempfield : fields) {

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

                if (tempClassType.equals("Default") || tempClassType.equals("") ) {
                    Field[] fields = tempClass.getDeclaredFields();
                    for (Field tempfield : fields) {
//                    System.out.println(tempfield.value());
                        if (tempfield.isAnnotationPresent(Autowired.class)) {
                            String targetName = tempfield.getType().getSimpleName();

                            for (Map<String, Object> annotationchilds : ioclist) {
                                for (String annotationchildkv : annotationchilds.keySet()) {
                                    if (annotationchilds.get(annotationchildkv).getClass().getSimpleName().equals(targetName)) {

                                        tempfield.setAccessible(true);
                                        try {
                                            tempfield.set(tempObject, annotationchilds.get(targetName));
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
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


                if(tempClassType.equals("Aspect")) {
                    Method[] Methods = tempClass.getMethods();
                    for (Method method : Methods)
                    {
                        Before[] filters = method.getAnnotationsByType(Before.class);
                        Annotation[][] tttt= method.getParameterAnnotations();
                        Parameter[] parameters = method.getParameters();
                        Class[] parameterTypes = method.getParameterTypes();
                        Advice AfterAdvice = null;
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
                                            Method aopmethod =  tempClass.getMethod(filter.value()[1].toString(),null);
                                            AfterAdvice = new BeforeAdvice(tempObject2,  aopmethod,tempObject);
                                            Object helloServiceProxy = UtilsScan.getProxy(tempObject2, AfterAdvice);

                                            map.put(tempClass2.getSimpleName() , helloServiceProxy);
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //处理请求
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

        //方法的参数列表
        for (int i = 0; i<parameterTypes.length; i++){
            //根据参数名称，做某些处理
            String requestParam = parameterTypes[i].getSimpleName();


            if (requestParam.equals("HttpServletRequest")){
                //参数类型已明确，这边强转类型
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
                    paramValues[i]=value;
                }
            }
        }
        //利用反射机制来调用
        try {
            method.invoke(this.controllerMap.get(url), paramValues);//第一个参数是method所对应的实例 在ioc容器中
        } catch (Exception e) {
            e.printStackTrace();
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
    public static void main(String[] args) {
        UtilsScan.doScan();
    }

}
