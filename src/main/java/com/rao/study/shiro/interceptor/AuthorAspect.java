package com.rao.study.shiro.interceptor;

import com.rao.study.shiro.annotation.NoNeedAuth;
import com.rao.study.shiro.utils.HttpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@Aspect //注明为切面
public class AuthorAspect {

    @Around(value = "execution(@(org.springframework.web.bind.annotation.RequestMapping || org.springframework.web.bind.annotation.PostMapping || org.springframework.web.bind.annotation.GetMapping) * *(..))")
    public Object authorAspect(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable{
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if(!method.isAnnotationPresent(NoNeedAuth.class)){

            //通过RequestContextHolder获取ServletRequest
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            HttpServletResponse response = requestAttributes.getResponse();

            if(!HttpUtils.isValidateToken(request)){
                HttpUtils.noAuthorResponse(response);
                throw new RuntimeException("");
            }else{
                return proceedingJoinPoint.proceed();
            }
        }else{
            return proceedingJoinPoint.proceed();
        }
    }

}
