package com.qingcha.rpc.client.proxy;

/**
 * 默认代理池工厂
 *
 * @author qiqiang
 * @date 2020-11-04 9:22 上午
 */
public class DefaultProxyPoolFactory implements ProxyPoolFactory {

    private final String packagePath;

    public DefaultProxyPoolFactory(String packagePath) {
        this.packagePath = packagePath;
    }

    @Override
    public ProxyPool getProxyPool() {
        DefaultProxyPool defaultProxyPool = new DefaultProxyPool();
        defaultProxyPool.setPackagePath(packagePath);
        defaultProxyPool.refresh();
        return defaultProxyPool;
    }
}