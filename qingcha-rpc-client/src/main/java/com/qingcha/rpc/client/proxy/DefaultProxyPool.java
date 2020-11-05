package com.qingcha.rpc.client.proxy;

import com.qingcha.rpc.core.utils.ClassScanner;
import com.qingcha.rpc.core.utils.UsefulUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认代理池
 *
 * @author qiqiang
 * @date 2020-11-04 9:22 上午
 */
public class DefaultProxyPool extends AbstractProxyPool {
    private Map<String, RpcClientHolder> proxyInfoMap;
    private String packagePath;

    @Override
    public RpcClientHolder getProxy(String className) {
        return proxyInfoMap.get(className);
    }

    @Override
    protected Map<String, RpcClientHolder> getRpcClientHolderMap() {
        return proxyInfoMap;
    }

    @Override
    public void refresh() {
        if (UsefulUtils.isBlack(packagePath)) {
            return;
        }
        List<Class<?>> classList = ClassScanner.scan(packagePath);
        proxyInfoMap = new ConcurrentHashMap<>(classList.size() * 2);
        this.deal(classList);
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }
}