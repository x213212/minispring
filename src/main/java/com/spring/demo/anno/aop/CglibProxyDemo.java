package com.spring.demo.anno.aop;


import com.spring.demo.impl.IndexServiceimpl;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyDemo {
    static class Original {
        public void originalMethod(String s) {
            System.out.println(s);
        }
    }
    static class Handler implements MethodInterceptor {
        private final IndexServiceimpl original;
        public Handler(IndexServiceimpl original) {
            this.original = original;
        }
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            System.out.println("BEFORE");
            method.invoke(original, args);
            System.out.println("AFTER");
            return null;
        }
    }
    public static void main(String[] args){
        IndexServiceimpl original = new IndexServiceimpl();
        MethodInterceptor handler = new Handler(original);
        IndexServiceimpl f = (IndexServiceimpl) Enhancer.create(IndexServiceimpl.class,handler);
        f.index();
    }
}