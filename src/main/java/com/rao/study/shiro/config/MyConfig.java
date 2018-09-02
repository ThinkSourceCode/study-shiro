package com.rao.study.shiro.config;


import com.rao.study.shiro.realm.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.Factory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean
    public DefaultSecurityManager defaultSecurityManager(Realm myRealm){
        Factory<SecurityManager> factory = new IniSecurityManagerFactory();
        //获取安全管理器,整个shiro的核心
        DefaultSecurityManager securityManager = (DefaultSecurityManager) factory.getInstance();
        securityManager.setRealm(myRealm);//设置自定义的Realm

        //将安全管理器保持到一个全局变量中,供整个项目的使用
        SecurityUtils.setSecurityManager(securityManager);

        return securityManager;
    }



    @Bean("myRealm")
    public Realm realm(){
        MyRealm myRealm = new MyRealm();
        return myRealm;
    }
}
