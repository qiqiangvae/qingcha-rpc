package com.qingcha.rpc.client.proxy;

/**
 * 代理池工厂接口，默认实现类是 {@link DefaultProxyPoolFactory}
 *
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