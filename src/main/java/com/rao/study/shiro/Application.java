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
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");//这里采用的是IniRealm,系统自带的Realm
        //获取安全管理器,整个shiro的核心
        SecurityManager securityManager = factory.getInstance();

        //将安全管理器保持到一个全局变量中,供整个项目的使用
        SecurityUtils.setSecurityManager(securityManager);

        Subject currentUser = SecurityUtils.getSubject();

        //仅仅进行身份认证,不进行授权,所以配置文件中只配置了用户名和密码
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("lonestarr","vespa");

        try {
            currentUser.login(usernamePasswordToken);//当这里调用时,会使用系统的Realm对token进行验证,在ModularRealmAuthenticator类中的org.apache.shiro.realm.Realm.getAuthenticationInfo这个方法进行验证
        }catch (AuthenticationException e){
            log.info("身份验证失败");
        }
        if(currentUser.isAuthenticated()){
            log.info("已经登录");
        }
        currentUser.logout();
        System.exit(0);
    }
}
