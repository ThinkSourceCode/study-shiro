package com.rao.study.shiro;


import com.rao.study.shiro.realm.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final transient Logger log = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args){
        log.info("My First Apache Shiro Application");

        MyRealm myRealm = new MyRealm();

        Factory<SecurityManager> factory = new IniSecurityManagerFactory();
        //获取安全管理器,整个shiro的核心
        DefaultSecurityManager securityManager = (DefaultSecurityManager) factory.getInstance();
        securityManager.setRealm(myRealm);//设置自定义的Realm

        //将安全管理器保持到一个全局变量中,供整个项目的使用
        SecurityUtils.setSecurityManager(securityManager);

        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");

        try {
            currentUser.login(token); //因为没有自定义realm，所以会使用系统的AuthorizingRealm进行登陆验证
            log.info("登陆成功");
        }catch (AuthenticationException e){
            log.info("登陆失败");
        }
        //判断是否有某角色
        if(currentUser.hasRole("superAdmin")){//使用系统的AuthorizingRealm进行授权验证
            log.info("属于超级管理员");
        }else{
            log.info("不属于超级管理员");
        }

        //判断用户是否拥有某个权限
        if (currentUser.isPermitted("user:delete")){
            log.info("拥有user:delete权限");
        }else{
            log.info("没有user:delete权限");
        }

        //或者使用check的方式
        try{
            currentUser.checkPermission("user:delete");//有某个权限不抛出异常,没有则抛出异常
            log.info("拥有user:delete权限");
        }catch (AuthorizationException e){
            log.info("没有user:delete权限");
        }

        //在spring下,可以使用@RequirePermission注解,在jsp中可以使用相应的shiro标签

        System.exit(0);
    }

}
