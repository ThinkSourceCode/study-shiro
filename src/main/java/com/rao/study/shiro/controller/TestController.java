package com.rao.study.shiro.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    //默认情况下,普通的请求是不会被拦截的
    @GetMapping("/test1")
    public String test1(){
        return "test1";
    }

    //添加shiro的权限注解,需要配置aop的支持
    @RequiresPermissions("user:create")
    @GetMapping("/test2")
    public String test2(){
        return "test2";
    }

    @RequiresPermissions("ssf")//随便写的一个权限
    @GetMapping("/test3")
    public String test3(){
        return "test3";
    }
}
