package com.rao.study.shiro.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthoTestController {

    //使用shiro的权限注解,这个权限在Realm中加载
    @RequiresPermissions("channel-add")
    @GetMapping("/testRequire")
    public void testAuthor1(){
        System.out.println("testAuthor1");
    }

    @RequiresRoles("channel-super-admin")
    @GetMapping("/testRequire1")
    public void testAuthor2(){
        System.out.println("testAuthor2");
    }

}
