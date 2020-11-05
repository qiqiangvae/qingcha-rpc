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
    /**
     * 保存调用结果的容器
     */
    private final Map<String, ArrayBlockingQueue<Object>> responseMap = new ConcurrentHashMap<>();
    /**
     * 保存当前等待结果的线程的容器
     */
    private final Map<String, Thread> threadMap = new ConcurrentHashMap<>();
    /**
     * 保存出现调用异常的容器
     */
    private final Map<String, Throwable> throwableMap = new ConcurrentHashMap<>();

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
            // 将当前线程放到 threadMap 中
            threadMap.put(id, Thread.currentThread());
            return queue.poll(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // 封装并抛出异常
            throw new RpcResponseException(throwableMap.get(id));
        } finally {
            // 帮助垃圾回收
            responseMap.remove(id);
            throwableMap.remove(id);
            responseMap.remove(id);
        }
    }

    /**
     * 抛出异常
     *
     * @param id        请求 key
     * @param throwable 异常
     */
    protected void interrupt(String id, Throwable throwable) {
        Thread thread = threadMap.get(id);
        if (thread != null) {
            // 保存，然后打断等待线程
            throwableMap.put(id, throwable);
            // 线程中断，从 com.qingcha.rpc.client.proxy.RpcClientHolder.get queue.poll(1, TimeUnit.MINUTES) 处被唤醒
            thread.interrupt();
        }
    }

    protected ArrayBlockingQueue<Object> getQueue(String id) {
        ArrayBlockingQueue<Object> queue = responseMap.getOrDefault(id, new ArrayBlockingQueue<>(1));
        responseMap.put(id, queue);
        return queue;
    }

}