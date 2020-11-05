package com.qingcha.rpc.client.proxy;

/**
 * 代理池接口，默认实现类是{@link DefaultProxyPool}
 *
 * @author qiqiang
 * @date 2020-11-04 9:18 上午
 */
public interface ProxyPool {
    /**
     * 获取代理对象 RpcClientHolder
     *
     * @param className 类命
     * @return RpcClientHolder
     */
    RpcClientHolder getProxy(String className);
}