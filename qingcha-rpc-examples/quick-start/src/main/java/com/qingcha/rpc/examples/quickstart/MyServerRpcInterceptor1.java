package com.qingcha.rpc.examples.quickstart;

import com.qingcha.rpc.core.interceptor.RpcInterceptorChain;
import com.qingcha.rpc.server.interceptor.ServerInterceptor;
import com.qingcha.rpc.core.interceptor.ShareInterceptor;
import com.qingcha.rpc.server.invoke.InvokeRequest;

/**
 * @author qiqiang
 * @date 2020-11-26 15:58
 */
@ShareInterceptor
public class MyServerRpcInterceptor1 implements ServerInterceptor {
    @Override
    public Object intercept(String id, InvokeRequest invokeRequest, RpcInterceptorChain<InvokeRequest> chain) throws Throwable {
        System.out.println("before MyServerRpcInterceptor1");
        final Object invoke = chain.invoke(id, invokeRequest);
        System.out.println("after MyServerRpcInterceptor1");
        return invoke;
    }
}