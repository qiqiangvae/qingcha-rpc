package com.qingcha.rpc.client.proxy;

import com.qingcha.rpc.client.RpcClientConfiguration;

import java.util.ServiceLoader;

/**
 * @author qiqiang
 * @date 2020-11-04 2:29 下午
 */
public class ProxyPoolManager {
    private static ProxyPool proxyPool;

    public static void setProxyPool(ProxyPool proxyPool) {
        ProxyPoolManager.proxyPool = proxyPool;
    }

    public static ProxyPool getProxyPool() {
        // 通过 SPI 机制获取 ProxyPoolFactory，从而获取代理池 proxyPool
        if (proxyPool == null) {
            ServiceLoader<ProxyPoolFactory> factories = ServiceLoader.load(ProxyPoolFactory.class);
            for (ProxyPoolFactory factory : factories) {
                if (factory != null) {
                    proxyPool = factory.getProxyPool();
                }
            }
            if (proxyPool == null) {
                RpcClientConfiguration configuration = RpcClientConfiguration.configuration();
                proxyPool = new DefaultProxyPoolFactory(configuration.getPackagePath()).getProxyPool();
            }
        }
        return proxyPool;
    }
}