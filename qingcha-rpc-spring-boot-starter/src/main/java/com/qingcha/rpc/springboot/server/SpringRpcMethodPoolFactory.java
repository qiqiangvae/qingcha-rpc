package com.qingcha.rpc.springboot.server;

import com.qingcha.rpc.server.invoke.MethodPool;
import com.qingcha.rpc.server.invoke.MethodPoolFactory;
import com.qingcha.rpc.server.invoke.MethodPoolManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

/**
 * @author qiqiang
 * @date 2020-11-04 3:04 下午
 */
public class SpringRpcMethodPoolFactory implements MethodPoolFactory, ApplicationContextAware, InitializingBean, EnvironmentAware {
    private ApplicationContext applicationContext;
    private SpringMethodPool springMethodPool;
    private String serviceAnnotation;
    private final static String DEFAULT_SERVICE_ANNOTATION = Service.class.getName();

    @Override
    public MethodPool getMethodPool() {
        return springMethodPool;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Class<?> clazz = Class.forName(serviceAnnotation);
        if (clazz.isAnnotation()) {
            springMethodPool = new SpringMethodPool(applicationContext, (Class<? extends Annotation>) clazz);
        } else {
            springMethodPool = new SpringMethodPool(applicationContext);
        }
        springMethodPool.refresh();
        MethodPoolManager.setMethodPool(springMethodPool);
    }

    @Override
    public void setEnvironment(Environment environment) {
        serviceAnnotation = environment.getProperty("com.qingcha.rpc.server.service-annotation", DEFAULT_SERVICE_ANNOTATION);
    }
}