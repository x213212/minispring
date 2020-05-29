package com.spring.demo.run;


import com.spring.demo.serivce.IndexService;
import com.spring.demo.utils.AnnotationConfigApplicationContext;
import org.apache.catalina.LifecycleException;


import java.io.IOException;


public class demo {

	public static void main(String[] args) throws LifecycleException {

		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext ();
		//SpringApplication.run(demo.class, args);

		IndexService service = (IndexService) annotationConfigApplicationContext.getBean("IndexServiceimpl");
		//IndexService service2 = (IndexService) annotationConfigApplicationContext.getBean("IndexServiceimpl2");
		for(int i = 0 ; i < 10 ; i ++)
		{
			service.index();
		}
		try

		{

			for(int i =0 ; i<10 ; i++)


			{

				System.out.println(i);

				System.in.read();

			}

		}

		catch(IOException e)

		{

		}


	}

}
