package com.rao.study.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;

//自定义Realm进行权限和登陆验证
public class MyRealm extends AuthorizingRealm {

    //假设为数据库中的数据
    private static Map<String ,String> users = new HashMap<String, String>();
    private static Map<String, List<String>> user_roles = new HashMap<String, List<String>>();
    private static Map<String,List<String>> role_permissions = new HashMap<String, List<String>>();

    {
        //用户
        users.put("zhang","123");
        users.put("wang","123");
        //用户及角色
        user_roles.put("zhang", Arrays.asList("admin","superAdmin"));
        user_roles.put("wang",Arrays.asList("admin"));

        //角色及权限
        role_permissions.put("admin",Arrays.asList("user:creat","user:update"));
        role_permissions.put("superAdmin",Arrays.asList("user:creat","user:update","user:delete"));
    }


    //权限验证  (当调用了进行权限验证的方法时,才会调用这个方法,通过这个方法返回对应用户的角色和权限)
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String) principalCollection.getPrimaryPrincipal();

        //权限验证,是将对应的用户的权限及角色查询出来,再构造成一个AuthorizationInfo对象返回

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //先取出用户对应的角色
        List<String> roles = user_roles.get(username);

        //在根据角色取出对应的所有权限
        Set<String> permissions = new HashSet<>();
        roles.forEach(role->{
            role_permissions.get(role).forEach(permission->{
                permissions.add(permission);
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

        //在这里可以去查询数据库,进行用户名和密码的登陆验证
        String pwd = users.get(username);
        if(pwd!=null && pwd.equals(password)){
            return new SimpleAuthenticationInfo(username,password,getName());//成功,则返回一个AuthenticationInfo对象,表示登陆验证成功
        }else{
            throw new AuthenticationException("登陆失败");
        }
    }
}
