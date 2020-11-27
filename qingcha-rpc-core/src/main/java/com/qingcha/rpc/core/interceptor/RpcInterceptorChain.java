package com.qingcha.rpc.core.interceptor;

import java.util.LinkedList;

/**
 * 拦截器链路
 *
 * @author qiqiang
 * @date 2020-11-26 16:22
 */
public class RpcInterceptorChain<T> {
    private final LinkedList<RpcInterceptor<T>> interceptors = new LinkedList<>();
    private final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public void addLastInterceptor(RpcInterceptor<T> interceptor) {
        this.interceptors.addLast(interceptor);
    }

    public Object invoke(String id, T target) throws Throwable {
        final RpcInterceptor<T> next = getNext();
        if (next == null) {
            throw new RpcInterceptorException("没有下一个 RpcInterceptor");
        }
        // 执行下一个拦截器
        return next.intercept(id, target, this);
    }

    /**
     * 获取下一个拦截器
     */
    private RpcInterceptor<T> getNext() {
        Integer integer = threadLocal.get();
        if (integer == null) {
            integer = 0;
        }
        final RpcInterceptor<T> next = interceptors.get(integer);
        threadLocal.set(integer + 1);
        return next;
    }

    public void clear() {
        threadLocal.remove();
    }
}