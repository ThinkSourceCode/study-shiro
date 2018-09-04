package com.rao.study.shiro.filter;

import org.apache.shiro.web.servlet.OncePerRequestFilter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

//整个过滤链中只调用一次,用来防止多次调用
public class MyOncePerRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws ServletException, IOException {
        System.out.println("=========once per request filter");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
