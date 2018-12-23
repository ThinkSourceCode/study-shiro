package com.rao.study.shiro.interceptor;

import com.rao.study.shiro.annotation.NoNeedAuth;
import com.rao.study.shiro.utils.HttpUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthorInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod  handlerMethod = (HandlerMethod) handler;
        Method  method = handlerMethod.getMethod();
        if(method.isAnnotationPresent(NoNeedAuth.class)){
            return super.preHandle(request,response,handler);
        }else{
            String token = HttpUtils.getTokenFromRequest(request);
            if(StringUtils.isEmpty(token)){
                noAuthorResponse(response);
                return false;
            }else{
                if(HttpUtils.isValidateToken(request)){
                    return super.preHandle(request,response,handler);
                }else{
                    noAuthorResponse(response);
                    return false;
                }
            }
        }
    }

    public void noAuthorResponse(HttpServletResponse response)throws Exception{
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(401);

        response.getWriter().print("无权访问");
    }
}

