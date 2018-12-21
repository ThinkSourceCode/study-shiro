package com.rao.study.shiro.realm;

import com.rao.study.shiro.domain.Role;
import com.rao.study.shiro.domain.User;
import com.rao.study.shiro.sql.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;
import java.util.stream.Collectors;

//自定义Realm进行权限和登陆验证
public class MyRealm extends AuthorizingRealm {

    //权限验证  (当调用了进行权限验证的方法时,才会调用这个方法,通过这个方法返回对应用户的角色和权限)
    //比如check方法或者requirepermission或者requirerole权限注解的时候会调用到,用来获取用户拥有的权限和角色
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        User user = (User) principalCollection.getPrimaryPrincipal();

        //权限验证,是将对应的用户的权限及角色查询出来,再构造成一个AuthorizationInfo对象返回

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //先取出用户对应的角色
        List<Role> roleList = SqlOperation.getRoles(user.getId());
        List<String> roles = roleList.stream().map(Role::getRolename).collect(Collectors.toList());

        //在根据角色取出对应的所有权限

        Set<String> permissions = new HashSet<String>();
        roleList.forEach(role -> {
            SqlOperation.getPermissions(role.getId()).stream().map(permission -> permission.getPermission()).collect(Collectors.toList()).forEach(permissionStr->{
                permissions.add(permissionStr);
            });
        });

        simpleAuthorizationInfo.addRoles(roles);//设置角色
        simpleAuthorizationInfo.addStringPermissions(permissions);//设置权限

        return simpleAuthorizationInfo;
    }

    //登陆验证 (调用了login方法时才调用这个方法)
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //因为使用的是AuthenticationUserToken校验授权,所以这里只能获取token的值了
        String token = (String) authenticationToken.getPrincipal();

        //根据用户名和密码查询
        User user = SqlOperation.loginByToken(token);
        if(user!=null){

            //这块存储的数据,在doGetAuthorizationInfo方法中用得到
            //比如这里的第一个参数,在doGetAuthorizationInfo的参数就可以获取,这里存储username,那么获取的就是username,如：String username = (String) principalCollection.getPrimaryPrincipal();
            //如果这里存储的是user,那么获取的就是一个user对象,User user = (User) principalCollection.getPrimaryPrincipal();
            return new SimpleAuthenticationInfo(user,token,getName());//成功,则返回一个AuthenticationInfo对象,表示登陆验证成功
        }else{
            throw new AuthenticationException("登陆失败");
        }
    }

    //表示只使用AuthenticationUserToken的校验才进行身份验证校验
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthenticationUserToken;
    }
}
