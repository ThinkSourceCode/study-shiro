package com.rao.study.shiro.config;


import com.rao.study.shiro.filter.MyAuthorizationFilter;
import com.rao.study.shiro.filter.MyOncePerRequestFilter;
import com.rao.study.shiro.realm.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.Factory;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyConfig {

    @Bean
    public SecurityManager defaultSecurityManager(){
        //获取安全管理器,整个shiro的核心,web应用则使用WebSecurityManager
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(new MyRealm());

        //将安全管理器保持到一个全局变量中,供整个项目的使用
        SecurityUtils.setSecurityManager(securityManager);



        return securityManager;
    }

    //通过ShiroFilterFactoryBean来管理shiro的过滤
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);

        //shiro的过滤器是采用的Servlet的filter进行代理过滤,所以是对url资源进行过滤的,所以使用过滤器,则需要指定这个过滤器对那些url资源进行过滤

        //1.先添加自定义的过滤器
        Map<String, Filter> filterMap = new HashMap<String, Filter>();
        filterMap.put("authFilter",new MyAuthorizationFilter());//每个过滤器都有一个名称,这样可以重复使用这个名称,通过名称key查找过滤实例
        filterMap.put("onceFilter",new MyOncePerRequestFilter());

        filterFactoryBean.setFilters(filterMap);

        //2.设置指定的url资源用相应的过滤器
        Map<String,String> filterChainDefinitionMap = new HashMap<String,String>();
        filterChainDefinitionMap.put("/webjars/**", "anon");//anon表示匿名,shiro自带的过滤器
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/app/**", "anon");
        filterChainDefinitionMap.put("/sys/login", "anon");
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/captcha.jpg", "anon");

        //使用自定义的过滤器
        filterChainDefinitionMap.put("/**","authFilter");//表示对所有的资源都使用authFilter过滤器进行过滤
        filterChainDefinitionMap.put("/register","onceFilter");

        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return filterFactoryBean;
    }

    /**
     * 开启对AOP的支持,Shiro中的权限注解使用了Spring
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /**
     * 添加对Shiro权限注解的拦截处理器,这些注解在new Class[] {
     *                     RequiresPermissions.class, RequiresRoles.class,
     *                     RequiresUser.class, RequiresGuest.class, RequiresAuthentication.class
     *             };
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


}
