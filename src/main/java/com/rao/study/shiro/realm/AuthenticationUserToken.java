package com.rao.study.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

//自定义用token校验登录的方式
public class AuthenticationUserToken implements AuthenticationToken {
    private String token;

    public AuthenticationUserToken(String token) {
        this.token = token;
    }

    /**
     * 下面两个方法getPrincipal和getCredentials都指明token,则到时获取的时候就只有token的值,这样就可以限制为token登录校验
     *
     */


    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
