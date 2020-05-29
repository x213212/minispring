package com.spring.demo.utils;


import com.spring.demo.serivce.IndexService;
import com.spring.demo.servlet.MyDispatcherServlet;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;

public class Test {
    public static void main(String[] args) throws LifecycleException, InterruptedException, ServletException {

//        java.io.File file = new java.io.File("." );
//
//        Tomcat tomcat = new Tomcat();
//        tomcat.setPort(8082);
//
//        Context ctx = tomcat.addContext("/", file.getAbsolutePath());
//        HttpServlet servlet = new MyDispatcherServlet();
//        String servletName = "MyDispatcherServlet";
//        String urlPattern = "/*";
////        Tomcat.addServlet(ctx, "MyDispatcherServlet", new HttpServlet() {
////            @Override
////            protected void service(HttpServletRequest req, HttpServletResponse resp)
////                    throws ServletException, IOException {
////
////                Writer w = resp.getWriter();
////                w.write("Embedded Tomcat servlet.\n");
////                w.flush();
////                w.close();
////            }
////        });
//        tomcat.addServlet(ctx, servletName, servlet);
//
//        ctx.addServletMapping("/*", urlPattern);
//
//        tomcat.start();
//        tomcat.getServer().await();


        ///

        //UtilsScan tmp = new UtilsScan();
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext ();
        IndexService service = (IndexService) annotationConfigApplicationContext.getBean("IndexServiceimpl");
        //IndexService service2 = (IndexService) annotationConfigApplicationContext.getBean("IndexServiceimpl2");
//        for(int i = 0 ; i < 10 ; i ++)
//        {
//                service.index();
//        }
//        try
//
//        {
//
//            for(int i =0 ; i<10 ; i++)
//
//
//            {
//
//                System.out.println(i);
//
//                System.in.read();
//
//            }
//
//        }
//
//        catch(IOException e)
//
//        {
//
//        }


        //System.out.println(service);
    }


}
