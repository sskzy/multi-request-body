package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : songtc
 * @since : 2023/12/14 11:45
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiRequestBody {
    /**
     * 解析时用到的 JSON的 key
     */
    String value() default "";

    /**
     * 是否必须出现的参数
     */
    boolean required() default true;
}
