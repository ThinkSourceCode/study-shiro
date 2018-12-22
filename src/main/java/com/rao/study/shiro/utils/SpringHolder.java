package com.rao.study.shiro.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext ctxt) throws BeansException {
        applicationContext = ctxt;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
