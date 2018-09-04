package com.rao.study.shiro.filter;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

//实现权限访问过滤
public class MyAuthorizationFilter extends AuthenticatingFilter {

    //用于处理那些资源可以访问,那些资源不可以访问,可以访问的返回true,不可以访问的返回false
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest rq = (HttpServletRequest) request;
        String method = rq.getMethod();
        String uri = WebUtils.getRequestUri(rq);
        if(uri.contains("login")){//表示指定什么方法可以通过，或者什么uri可以通过
            return true;
        }else{
            return false;
        }
    }

    //表示登陆成功,做什么
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        return super.onLoginSuccess(token, subject, request, response);
    }

    //表示登陆失败时做什么
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return super.onLoginFailure(token, e, request, response);
    }

    //自定义授权token
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return null;
    }

    //表示在授权拒绝时做什么
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        System.out.println("被拒绝了,需要。。");
        servletResponse.setContentType("application/json;charset=utf-8");
        servletResponse.getWriter().print("你被拒绝了");
        return false;
    }
}
