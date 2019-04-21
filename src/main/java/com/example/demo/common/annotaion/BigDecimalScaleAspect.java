package com.example.demo.common.annotaion;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by sam on 2019/4/10.
 */
@Aspect
@Component
public class BigDecimalScaleAspect {

    @Pointcut("@annotation(BigDecimailScale)")
    public void annotationPointcut() {

        System.out.println("切面内容");
    }


    @Around("annotationPointcut()")
    public Object aroundPointcut(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature =  (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println(methodSignature.getReturnType());
        BigDecimailScale annotation = method.getAnnotation(BigDecimailScale.class);
        int roundMode = annotation.roundMode();
        int scale = annotation.scale();

       int paramsCount = joinPoint.getArgs().length;
       if (paramsCount == 1){
           BigDecimal val = (BigDecimal) joinPoint.getArgs()[0];
           val = val.setScale(scale,roundMode);
           System.out.println("值为："+val);
           //将参数修改为新的返回
           return joinPoint.proceed(Arrays.asList(val).toArray());
       }
       //不带参数则是以原来的参数返回
       return joinPoint.proceed();
    }


}
