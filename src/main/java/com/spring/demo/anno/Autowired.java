package com.spring.demo.anno;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Autowired {

}

