package com.rao.study.shiro.auth;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 自定义Realm
 */
public class MyRealm extends AuthorizingRealm {//直接实现Realm接口的话，需要实现太多方法,在realm包下,shiro已经给我们实现了很多有用的Realm,比如jdbcRealm,我们直接提供查询sql即可,我们也可以自定义Realm，再返回查询的用户角色和用户角色对应的权限

    //授权 （返回权限和角色） 验证权限时调用
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //身份认证  登录时调用,在Subject.login调用时会调用这个方法进行验证，所以可以在这个方法中去查询数据库信息
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();//因为使用的是UsernamePasswordToken,在getPrincipal()方法返回的是username
        String password = new String((char[]) authenticationToken.getCredentials());//获取密码,因为getCredentials中返回的是字符数组,所以这里需要转换为字符串
        if(!"darkhelmet".equals(username)) {
            throw new UnknownAccountException(); //如果用户名错误
        }
        if(!"ludicrousspeed".equals(password)) {
            throw new IncorrectCredentialsException(); //如果密码错误
        }
        //如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        //指定固定的token,也可以自定义
        return token instanceof UsernamePasswordToken;//这里使用shiro提供的token，如果要自定义的token,则需要使用Filter,并将token注入到Filter中
    }
}
