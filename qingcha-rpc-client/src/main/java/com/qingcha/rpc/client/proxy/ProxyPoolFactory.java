package com.qingcha.rpc.client.proxy;

/**
 * @author qiqiang
 * @date 2020-11-03 3:10 下午
 */
public interface ProxyPoolFactory {
    /**
     * 获取方法池
     *
     * @return 方法池
     */
    ProxyPool getProxyPool();
}