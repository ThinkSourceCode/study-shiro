package com.rao.study.shiro.controller;

import com.rao.study.shiro.annotation.NoNeedAuth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    //默认情况下,普通的请求是不会被拦截的
    @GetMapping("/test1")
    public String test1(){
        return "test1";
    }

    //添加shiro的权限注解,需要配置aop的支持
    //同一个会话中能成功进行权限判断,如果是多个浏览器的话,会判断当前session,所以会导致拿到同一个token,在自定义的filter中返回true了,却仍然无法访问,此时就可以在被拒绝的地方做手脚
    @RequiresPermissions("user:create")
    @PostMapping("/test2")
    public String test2(){
        return "test2";
    }

    @RequiresPermissions("ssf")//随便写的一个权限
    @GetMapping("/test3")
    public String test3(){
        return "test3";
    }

    @NoNeedAuth
    @GetMapping("/test4")
    public String test4(){
        return "test4";
    }
}
