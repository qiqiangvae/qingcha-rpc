package com.qingcha.rpc.client.proxy;

import com.qingcha.rpc.client.RpcClient;

import java.util.List;
import java.util.Map;

/**
 * @author qiqiang
 * @date 2020-11-04 9:23 上午
 */
public abstract class AbstractProxyPool implements ProxyPool {
    /**
     * 获取容器
     *
     * @return 容器
     */
    abstract protected Map<String, RpcClientHolder> getRpcClientHolderMap();


    protected boolean checkClass(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        if (!clazz.isInterface()) {
            return false;
        }
        return true;
    }

    protected void deal(List<Class<?>> classList) {
        for (Class<?> clazz : classList) {
            // 检查有效性
            if (!checkClass(clazz)) {
                continue;
            }
            // 新建 rpc client和持有者
            RpcClient rpcClient = new RpcClient(clazz.getName());
            RpcClientHolder holder = new RpcClientHolder();
            holder.setRpcClient(rpcClient);
            holder.setClazz(clazz);
            rpcClient.setHolder(holder);
            getRpcClientHolderMap().put(clazz.getName(), holder);
        }
    }

    /**
     * 刷新容器
     */
    public abstract void refresh();
}