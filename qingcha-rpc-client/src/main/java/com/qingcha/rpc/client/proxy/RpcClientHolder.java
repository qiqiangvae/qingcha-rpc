package com.qingcha.rpc.client.proxy;

import com.qingcha.rpc.client.RpcClient;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author qiqiang
 * @date 2020-11-04 11:15 上午
 */
public class RpcClientHolder {
    private RpcClient rpcClient;
    private Class<?> clazz;
    private final Map<String, ArrayBlockingQueue<Object>> responseMap = new ConcurrentHashMap<>();

    public RpcClient getRpcClient() {
        return rpcClient;
    }

    public void setRpcClient(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Object get(String id) {
        ArrayBlockingQueue<Object> queue = getQueue(id);
        try {
            return queue.poll(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected ArrayBlockingQueue<Object> getQueue(String id) {
        ArrayBlockingQueue<Object> queue = responseMap.getOrDefault(id, new ArrayBlockingQueue<>(1));
        responseMap.put(id, queue);
        return queue;
    }

}