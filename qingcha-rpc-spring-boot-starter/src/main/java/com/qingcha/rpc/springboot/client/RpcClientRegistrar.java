package com.qingcha.rpc.springboot.client;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author qiqiang
 * @date 2020-11-04 4:01 下午
 */
public class RpcClientRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String SPRING_RPC_PROXY_POOL_FACTORY = "springRpcProxyPoolFactory";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        AbstractBeanDefinition springRpcProxyPoolFactoryDefinition = BeanDefinitionBuilder.genericBeanDefinition(SpringRpcProxyPoolFactory.class).getBeanDefinition();
        registry.registerBeanDefinition(SPRING_RPC_PROXY_POOL_FACTORY, springRpcProxyPoolFactoryDefinition);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    }
}