package com.example.demo.common.annotaion;

import java.lang.annotation.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * Created by sam on 2019/4/10.
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BigDecimailScale {

    public int roundMode() default ROUND_HALF_UP;

    public int scale() default 2;


}
