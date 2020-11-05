package com.qingcha.rpc.client.proxy;

/**
 * @author qiqiang
 * @date 2020-11-04 9:18 上午
 */
public interface ProxyPool {
    RpcClientHolder getProxy(String className);
}