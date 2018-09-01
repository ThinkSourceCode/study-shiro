package com.rao.study.shiro;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final transient Logger log = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args){
        log.info("My First Apache Shiro Application");

        //这里使用IniRealm读取ini文件,如果读取到[users]则会将其作为数据源保存到缓存中,当登陆时就将当前登录的用户信息和缓存中的信息进行匹配
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");//这里采用的是IniRealm,系统自带的Realm
        //获取安全管理器,整个shiro的核心
        SecurityManager securityManager = factory.getInstance();

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

        System.exit(0);
    }

}
