package com.qingcha.rpc.examples.quickstart;

import com.qingcha.rpc.core.interceptor.RpcInterceptorChain;
import com.qingcha.rpc.server.interceptor.ServerInterceptor;
import com.qingcha.rpc.server.invoke.InvokeRequest;

/**
 * @author qiqiang
 * @date 2020-11-26 15:58
 */
public class MyServerRpcInterceptor2 implements ServerInterceptor {
    @Override
    public Object intercept(String id, InvokeRequest invokeRequest, RpcInterceptorChain<InvokeRequest> chain) throws Throwable {
        System.out.println("before MyServerRpcInterceptor2");
        final Object invoke = chain.invoke(id, invokeRequest);
        System.out.println("after MyServerRpcInterceptor2");
        return invoke;
    }
}