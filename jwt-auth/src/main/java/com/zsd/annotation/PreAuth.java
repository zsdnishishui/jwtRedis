package com.zsd.annotation;

import java.lang.annotation.*;

/**
 * 权限注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreAuth {
    String value() default "";
}
