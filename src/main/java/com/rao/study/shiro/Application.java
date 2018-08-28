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
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //获取安全管理器,整个shiro的核心
        SecurityManager securityManager = factory.getInstance();

        //将安全管理器保持到一个全局变量中,供整个项目的使用
        SecurityUtils.setSecurityManager(securityManager);

        Subject currentUser = SecurityUtils.getSubject();

        Session session = currentUser.getSession();
        session.setAttribute("name","raoshihong");

        if(!currentUser.isAuthenticated()){
            //创建一个token
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("root","secret");
            usernamePasswordToken.setRememberMe(true);
            //当前用户以token登录
            try{
                currentUser.login(usernamePasswordToken);
                log.info("登陆成功"+currentUser.isAuthenticated());
            }catch ( UnknownAccountException uae ) {
                log.info("账户不存在");
            } catch ( IncorrectCredentialsException ice ) {
                //password didn't match, try again?
            } catch ( LockedAccountException lae ) {
                //account for that username is locked - can't login.  Show them a message?
            }catch ( AuthenticationException ae ) {
            //unexpected condition - error?
        }

        }

        System.exit(0);
    }
}
