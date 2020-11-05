package com.qingcha.rpc.springboot.client;

import com.qingcha.rpc.client.RpcClientConfiguration;
import com.qingcha.rpc.client.proxy.ProxyPool;
import com.qingcha.rpc.client.proxy.ProxyPoolFactory;
import com.qingcha.rpc.client.proxy.ProxyPoolManager;
import com.qingcha.rpc.server.invoke.MethodPoolManager;
import com.qingcha.rpc.springboot.server.SpringMethodPool;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

/**
 * @author qiqiang
 * @date 2020-11-04 3:04 下午
 */
public class SpringRpcProxyPoolFactory implements ProxyPoolFactory, BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private SpringProxyPool springProxyPool;

    private String packagePath;

    @Override
    public ProxyPool getProxyPool() {
        return springProxyPool;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        springProxyPool = new SpringProxyPool(registry);
        springProxyPool.setPackagePath(packagePath);
        springProxyPool.refresh();
        ProxyPoolManager.setProxyPool(springProxyPool);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        packagePath = environment.getProperty("qingcha.rpc.client.package-path");
        String host = environment.getProperty("qingcha.rpc.client.host", "localhost");
        String port = environment.getProperty("qingcha.rpc.client.port");
        if (StringUtils.isEmpty(port)) {
            throw new SpringRpcClientConfigurationException("配置[qingcha.rpc.client.port]不能为空！");
        }
        RpcClientConfiguration.configuration().setHost(host);
        RpcClientConfiguration.configuration().setPort(Integer.parseInt(port));
    }
}