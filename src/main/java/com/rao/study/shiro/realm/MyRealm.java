package com.rao.study.shiro.realm;

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
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String) principalCollection.getPrimaryPrincipal();

        //权限验证,是将对应的用户的权限及角色查询出来,再构造成一个AuthorizationInfo对象返回

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //先取出用户对应的角色
        List<Role> roleList = SqlOperation.getRoles(username);
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
        //这里的用户名和密码是在subject.login(token)时传递过来的
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[])authenticationToken.getCredentials());

        //根据用户名和密码查询
        if(SqlOperation.login(username,password)!=null){
            return new SimpleAuthenticationInfo(username,password,getName());//成功,则返回一个AuthenticationInfo对象,表示登陆验证成功
        }else{
            throw new AuthenticationException("登陆失败");
        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
}
