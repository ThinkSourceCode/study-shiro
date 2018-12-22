package com.rao.study.shiro.config;

import com.rao.study.shiro.filter.AuthorFilter;
import com.rao.study.shiro.realm.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean
    public SecurityManager defaultSecurityManager(SessionManager sessionManager){
        //获取安全管理器,整个shiro的核心,web应用则使用WebSecurityManager
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(new MyRealm());
        securityManager.setSessionManager(sessionManager);

        //将安全管理器保持到一个全局变量中,供整个项目的使用
//        SecurityUtils.setSecurityManager(securityManager);

        return securityManager;
    }

    //通过ShiroFilterFactoryBean来管理shiro
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);

        //添加过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authorFilter",new AuthorFilter());

        filterFactoryBean.setFilters(filterMap);

        //添加过滤器对url资源的映射
        Map<String,String> filterChainDefinitionMap = new HashMap<>();
        filterChainDefinitionMap.put("/**","authorFilter");//表示authorFilter这个过滤器对所有资源进行拦截(有些静态资源可以直接设置为匿名的nano)
        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return filterFactoryBean;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
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
