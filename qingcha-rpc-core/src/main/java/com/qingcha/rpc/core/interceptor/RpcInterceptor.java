package com.qingcha.rpc.core.interceptor;

/**
 * @author qiqiang
 * @date 2020-11-26 15:18
 */
public interface RpcInterceptor<T> {
    Object intercept(String id, T t, RpcInterceptorChain<T> chain) throws Throwable;
}