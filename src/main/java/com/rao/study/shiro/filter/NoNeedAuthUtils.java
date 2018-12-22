package com.rao.study.shiro.filter;

import com.rao.study.shiro.annotation.NoNeedAuth;
import com.rao.study.shiro.utils.SpringHolder;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;

public class NoNeedAuthUtils  {

    public static Boolean hasNoAuthentication(HttpServletRequest request){
        try {
            //这里一定要通过名称来获取,否则有可能会出现多实例
            RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) SpringHolder.getApplicationContext().getBean("requestMappingHandlerMapping");
            HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);
            HandlerMethod obj = (HandlerMethod) handler.getHandler();
            return AnnotatedElementUtils.isAnnotated(obj.getMethod(), NoNeedAuth.class);
        }catch (Exception e){
        }
        return false;
    }

}
