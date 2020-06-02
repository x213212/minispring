package com.spring.demo.anno;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepositoryTest {
    String[] value() default "";
}