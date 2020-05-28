package com.spring.demo.utils;

import com.spring.demo.anno.*;
import com.spring.demo.anno.aop.*;
import com.spring.demo.aop.IndexAop;
import com.spring.demo.serivce.IndexService;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.*;


public class UtilsScan {
    public static List<Map <String , Object>> ioclist = new ArrayList<>();
    public static List<Map <String , Object>> proxyioclist = new ArrayList<>();
    public  static void doScan(){
        String packagePath =  "D:\\Programming\\spring\\simplespringioc\\src\\main\\java\\com\\spring\\demo";
        File file = new File (packagePath );
        String[] childFile = file.list();
        for (String fileName : childFile) {
            //System.out.println(fileName);
            File childfiletmp = new File( packagePath +"\\" +fileName);
            String classFileName[] = childfiletmp.list();
            for (String className : classFileName ){
                if(className.equals("aop") )
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

    }
    public static void checkAnnotation()  {
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

    public static Object getProxy(Object bean, Advice advice) {
        return Proxy.newProxyInstance(UtilsScan.class.getClassLoader(),
                bean.getClass().getInterfaces(), advice);
    }
    public static void main(String[] args) {
        UtilsScan.doScan();
    }

}
