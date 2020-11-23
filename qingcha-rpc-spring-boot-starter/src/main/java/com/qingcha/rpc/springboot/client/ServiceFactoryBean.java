package com.qingcha.rpc.springboot.client;

import com.qingcha.rpc.client.proxy.ProxyProcessor;
import org.springframework.beans.factory.FactoryBean;

/**
 * 客户端代理服务 factory bean
 * @author qiqiang
 * @date 2020-11-04 4:04 下午
 */
public class ServiceFactoryBean<T> implements FactoryBean<T> {
    private Class<T> clazz;

    @Override
    public T getObject() throws Exception {
        ProxyProcessor proxyProcessor = ProxyProcessor.instance();
        return proxyProcessor.getProxy(clazz);
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}