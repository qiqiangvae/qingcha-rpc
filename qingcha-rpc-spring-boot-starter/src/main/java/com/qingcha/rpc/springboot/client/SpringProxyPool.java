package com.qingcha.rpc.springboot.client;

import com.qingcha.rpc.client.proxy.AbstractProxyPool;
import com.qingcha.rpc.client.proxy.RpcClientHolder;
import com.qingcha.rpc.core.utils.ClassScanner;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiqiang
 * @date 2020-11-04 3:58 下午
 */
public class SpringProxyPool extends AbstractProxyPool {

    private Map<String, RpcClientHolder> proxyInfoMap;
    private String packagePath;
    private BeanDefinitionRegistry registry;

    public SpringProxyPool(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected Map<String, RpcClientHolder> getRpcClientHolderMap() {
        return proxyInfoMap;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    @Override
    public void refresh() {
        if (null == packagePath || packagePath.isEmpty()) {
            return;
        }
        List<Class<?>> classList = ClassScanner.scan(packagePath);
        proxyInfoMap = new ConcurrentHashMap<>(classList.size() * 2);
        this.deal(classList);
        for (RpcClientHolder holder : proxyInfoMap.values()) {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(ServiceFactoryBean.class).getBeanDefinition();
            beanDefinition.getPropertyValues().add("clazz", holder.getClazz());
            String beanName = holder.getClazz().getSimpleName();
            beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    @Override
    public RpcClientHolder getProxy(String className) {
        return proxyInfoMap.get(className);
    }
}