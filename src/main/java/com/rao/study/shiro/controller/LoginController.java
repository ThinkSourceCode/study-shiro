package com.rao.study.shiro.controller;

import com.rao.study.shiro.dto.UserDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto){

        try {
            UsernamePasswordToken authenticationUserToken = new UsernamePasswordToken(userDto.getUsername(),userDto.getPassword());
            SecurityUtils.getSubject().login(authenticationUserToken);
            return "登陆成功";
        }catch (Exception e){
            return "登陆失败";
        }

    }

}
