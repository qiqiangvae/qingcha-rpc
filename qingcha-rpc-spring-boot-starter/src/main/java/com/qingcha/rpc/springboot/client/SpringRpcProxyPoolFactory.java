package com.qingcha.rpc.springboot.client;

import com.qingcha.rpc.client.RpcClientConfiguration;
import com.qingcha.rpc.client.proxy.ProxyPool;
import com.qingcha.rpc.client.proxy.ProxyPoolFactory;
import com.qingcha.rpc.client.proxy.ProxyPoolManager;
import com.qingcha.rpc.core.utils.UsefulUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * Spring 代理池工厂
 *
 * @author qiqiang
 * @date 2020-11-04 3:04 下午
 */
public class SpringRpcProxyPoolFactory implements ProxyPoolFactory, BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private static final String PACKAGE_PATH_KEY = "qingcha.rpc.client.package-path";
    private static final String HOST_KEY = "qingcha.rpc.client.host";
    private static final String PORT_KEY = "qingcha.rpc.client.port";

    private final String PROXY_POOL_BEAN_NAME = ProxyPool.class.getName();
    private SpringProxyPool proxyPool;

    private String packagePath;

    @Override
    public ProxyPool getProxyPool() {
        return proxyPool;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        proxyPool = new SpringProxyPool(registry);
        proxyPool.setPackagePath(packagePath);
        proxyPool.refresh();
        ProxyPoolManager.setProxyPool(proxyPool);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerSingleton(PROXY_POOL_BEAN_NAME, proxyPool);
    }

    @Override
    public void setEnvironment(Environment environment) {
        packagePath = environment.getProperty(PACKAGE_PATH_KEY);
        String host = environment.getProperty(HOST_KEY, "localhost");
        String port = environment.getProperty(PORT_KEY, "9200");
        if (UsefulUtils.isBlack(port)) {
            throw new SpringRpcClientConfigurationException("配置[qingcha.rpc.client.port]不能为空！");
        }
        RpcClientConfiguration.configuration().setHost(host);
        RpcClientConfiguration.configuration().setPort(Integer.parseInt(port));
    }
}