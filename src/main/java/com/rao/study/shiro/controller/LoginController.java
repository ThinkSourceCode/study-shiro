package com.rao.study.shiro.controller;

import com.rao.study.shiro.pojo.UserInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody UserInfo userInfo){
        System.out.println("");
    }
}
