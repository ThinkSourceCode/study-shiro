package com.rao.study.shiro.auth;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

//多Realm
public class My2Realm extends AuthorizingRealm {//一般开发,只需要实现AuthorizingRealm即可


    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //验证身份,在登录时调用
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        if(!username.equals("1")){
            throw new AuthenticationException("用户名不是1");
        }
        return new SimpleAuthenticationInfo(username,password,getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
}
