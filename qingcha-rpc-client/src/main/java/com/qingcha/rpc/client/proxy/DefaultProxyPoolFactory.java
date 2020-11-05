package com.qingcha.rpc.client.proxy;

/**
 * @author qiqiang
 * @date 2020-11-04 9:22 上午
 */
public class DefaultProxyPoolFactory implements ProxyPoolFactory {

    private String packagePath;

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