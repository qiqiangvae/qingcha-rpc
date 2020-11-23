package com.qingcha.rpc.springboot.server;

import com.qingcha.rpc.server.invoke.MethodPool;
import com.qingcha.rpc.server.invoke.MethodPoolFactory;
import com.qingcha.rpc.server.invoke.MethodPoolManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

/**
 * Spring 方法池工厂
 *
 * @author qiqiang
 * @date 2020-11-04 3:04 下午
 */
public class SpringRpcMethodPoolFactory implements MethodPoolFactory, ApplicationContextAware, InitializingBean, EnvironmentAware {
    private final String METHOD_POOL_BEAN_NAME = MethodPool.class.getName();
    private SpringMethodPool methodPool;

    private static final String SERVICE_ANNOTATION_KEY = "com.qingcha.rpc.server.service-annotation";
    private final String DEFAULT_SERVICE_ANNOTATION = Service.class.getName();
    private String serviceAnnotation;

    private ApplicationContext applicationContext;

    @Override
    public MethodPool getMethodPool() {
        return methodPool;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Class<?> clazz = Class.forName(serviceAnnotation);
        if (clazz.isAnnotation()) {
            methodPool = new SpringMethodPool(applicationContext, (Class<? extends Annotation>) clazz);
        } else {
            methodPool = new SpringMethodPool(applicationContext);
        }
        methodPool.refresh();
        MethodPoolManager.setMethodPool(methodPool);
        // 注入 Spring
        if (applicationContext instanceof ConfigurableListableBeanFactory) {
            ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) applicationContext;
            beanFactory.registerSingleton(METHOD_POOL_BEAN_NAME, methodPool);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        serviceAnnotation = environment.getProperty(SERVICE_ANNOTATION_KEY, DEFAULT_SERVICE_ANNOTATION);
    }
}