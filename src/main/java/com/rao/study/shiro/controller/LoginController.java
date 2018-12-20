package com.rao.study.shiro.controller;

import com.rao.study.shiro.pojo.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public String login(@RequestBody UserInfo userInfo){
        UsernamePasswordToken token = new UsernamePasswordToken(userInfo.getUsername(),userInfo.getPassword());
        Subject currentUser = SecurityUtils.getSubject();

        try {
            currentUser.login(token); //因为没有自定义realm，所以会使用系统的AuthorizingRealm进行登陆验证
            System.out.println("登录成功");
            //成功进行回调
            return "登录成功";
        }catch (AuthenticationException e){
            System.out.println("登陆失败");
            return "登录失败";
        }
    }
}
