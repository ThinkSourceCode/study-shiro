package com.rao.study.shiro.realm;

import com.rao.study.shiro.pojo.Permission;
import com.rao.study.shiro.pojo.Role;
import com.rao.study.shiro.pojo.UserInfo;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;
import java.util.stream.Collectors;

//自定义Realm进行权限和登陆验证
public class MyRealm extends AuthorizingRealm {

    public static List<UserInfo> userInfos = new ArrayList<>();//这些数据可以存到数据库中,通过sql进行查询
    static {

        //第一个用户
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("rao");
        userInfo.setPassword("123");

        List<Role> roles = new ArrayList<>();

        Role role = new Role();
        role.setRoleName("channel-admin");
        List<Permission> pems = new ArrayList<>();
        Permission permission = new Permission();
        permission.setPermName("channel-add");
        pems.add(permission);
        Permission permission1 = new Permission();
        permission1.setPermName("channel-update");
        pems.add(permission1);
        role.setPermissions(pems);
        roles.add(role);

        Role role1 = new Role();
        role1.setRoleName("operation-admin");
        List<Permission> pems2 = new ArrayList<>();
        Permission permission2 = new Permission();
        permission2.setPermName("channel-add");
        pems2.add(permission2);
        Permission permission3 = new Permission();
        permission3.setPermName("channel-update");
        pems2.add(permission3);
        role1.setPermissions(pems2);
        roles.add(role1);

        userInfo.setRoles(roles);
        userInfos.add(userInfo);

        //第二个用户

        UserInfo userInfo1 = new UserInfo();
        userInfo1.setUsername("shihong");
        userInfo1.setPassword("123");

        List<Role> roles4 = new ArrayList<>();

        Role role4 = new Role();
        role4.setRoleName("channel-super-admin");
        List<Permission> pems4 = new ArrayList<>();
        Permission permission4 = new Permission();
        permission4.setPermName("channel-add");
        pems4.add(permission4);
        Permission permission5 = new Permission();
        permission5.setPermName("channel-update");
        pems4.add(permission5);
        Permission permission4_2 = new Permission();
        permission4_2.setPermName("channel-delete");
        pems4.add(permission4_2);
        Permission permission4_3 = new Permission();
        permission4_3.setPermName("channel-query");
        pems4.add(permission4_3);
        role4.setPermissions(pems4);
        roles4.add(role4);

        Role role6 = new Role();
        role6.setRoleName("operation-super-admin");
        List<Permission> pems6 = new ArrayList<>();
        Permission permission6 = new Permission();
        permission6.setPermName("operation-add");
        pems6.add(permission6);
        Permission permission7_1 = new Permission();
        permission7_1.setPermName("operation-delete");
        pems6.add(permission7_1);
        Permission permission7_2 = new Permission();
        permission7_2.setPermName("operation-update");
        pems6.add(permission7_2);
        Permission permission7_3 = new Permission();
        permission7_3.setPermName("operation-query");
        pems6.add(permission7_3);
        role6.setPermissions(pems6);
        roles4.add(role6);

        userInfo1.setRoles(roles4);
        userInfos.add(userInfo1);

        System.out.println(userInfos);
    }

    //权限验证  (当调用了进行权限验证的方法时,才会调用这个方法,通过这个方法返回对应用户的角色和权限,比如调用 getSubject().checkRole()方法或者使用权限注解会调用这个方法,比如：RequiresPermissions)
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        String username = (String) principalCollection.getPrimaryPrincipal();

        //权限验证,是将对应的用户的权限及角色查询出来,再构造成一个AuthorizationInfo对象返回

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //先取出用户对应的角色
        for(UserInfo userInfo : userInfos){
            if(userInfo.getUsername().equals(username)){
                userInfo.getRoles();
            }
        }
        List<Role> roleList = userInfos.stream().filter(userInfo -> userInfo.getUsername().equals(username)).findAny().get().getRoles();

        List<String> roles = roleList.stream().map(role -> role.getRoleName()).collect(Collectors.toList());

        //再根据角色取出对应的所有权限
        Set<String> permissions = new HashSet<>();
        roleList.stream().forEach(role -> {
            Set<String> subPermissions = role.getPermissions().stream().map(Permission::getPermName).collect(Collectors.toSet());
            permissions.addAll(subPermissions);
        });

        simpleAuthorizationInfo.addRoles(roles);//设置角色
        simpleAuthorizationInfo.addStringPermissions(permissions);//设置权限

        return simpleAuthorizationInfo;
    }

    //登陆验证 (调用了Subject.login方法时才调用这个方法)
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //这里的用户名和密码是在subject.login(token)时传递过来的
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[])authenticationToken.getCredentials());

        //根据用户名

        if(userInfos.stream().anyMatch(userInfo -> userInfo.getUsername().equals(username)&&userInfo.getPassword().equals(password))){

            //登录成功后,会将这个用户名密码保存起来,存到当前会话中,当访问其他页面时,就会从SecurityUtils.getSubject()获取当前登录的用户信息,则我们在进行验证权限,比如上面那个doGetAuthorizationInfo方法时,就可以从PrincipalCollection中获取相关信息
            return new SimpleAuthenticationInfo(username,password,getName());//成功,则返回一个AuthenticationInfo对象,表示登陆验证成功
        }else{
            throw new AuthenticationException("登陆失败");
        }
    }

    //指明支持哪种验证token的方式,这里指明通过用户名和密码来验证是否登录
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
}
