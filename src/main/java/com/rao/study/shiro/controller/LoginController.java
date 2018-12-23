package com.rao.study.shiro.controller;

import com.rao.study.shiro.annotation.NoNeedAuth;
import com.rao.study.shiro.domain.User;
import com.rao.study.shiro.dto.UserDto;
import com.rao.study.shiro.realm.AuthenticationUserToken;
import com.rao.study.shiro.sql.SqlOperation;
import com.rao.study.shiro.utils.TokenGenerator;
import com.rao.study.shiro.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    @NoNeedAuth
    public UserVo login(@RequestBody UserDto userDto){
        UserVo userVo = new UserVo();
        userVo.setCode(500);
        userVo.setMsg("登录不成功");
        try {

            //通过用户名和密码进行查询这个用户,或者通过手机号和验证码验证通过后生成token进行shiro的权限登录
            User user = SqlOperation.login(userDto.getUsername(),userDto.getPassword());
            if(user == null){
                return userVo;
            }
            //使用token进行shiro登录
            String token = TokenGenerator.generateValue();
            user.setToken(token);
            SqlOperation.saveUserToken(user,token);
            //将生成的token保存到用户数据库中

            AuthenticationToken authenticationToken = new AuthenticationUserToken(token);
            SecurityUtils.getSubject().login(authenticationToken);

            userVo.setCode(200);
            userVo.setMsg("登录成功");
            userVo.setData(user);
            return userVo;
        }catch (Exception e){
            return userVo;
        }

    }

}
