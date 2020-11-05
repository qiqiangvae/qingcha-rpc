package com.qingcha.rpc.client.proxy;

import java.lang.reflect.Proxy;

/**
 * @author qiqiang
 * @date 2020-11-04 9:35 上午
 */
public class ProxyProcessor {
    public static ProxyProcessor instance() {
        return ProxyProcessorHolder.INSTANCE;
    }

    public <T> T getProxy(Class<T> clazz) {
        // 从代理池中获取 rpc client 持有者
        RpcClientHolder holder = ProxyPoolManager.getProxyPool().getProxy(clazz.getName());
        if (holder == null) {
            throw new NoSuchServiceException(clazz.getName());
        }
        ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler(holder);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxyInvocationHandler);
    }

    static class ProxyProcessorHolder {
        private static final ProxyProcessor INSTANCE = new ProxyProcessor();
    }

}