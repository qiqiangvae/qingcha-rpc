package com.qingcha.rpc.examples.quickstart;

import com.qingcha.rpc.client.interceptor.ClientInterceptor;
import com.qingcha.rpc.core.interceptor.RpcInterceptorChain;
import com.qingcha.rpc.core.protocol.RpcProtocol;

/**
 * @author qiqiang
 * @date 2020-11-26 15:58
 */
public class MyClientRpcInterceptor2 implements ClientInterceptor {
    @Override
    public Object intercept(String id, RpcProtocol rpcProtocol, RpcInterceptorChain<RpcProtocol> chain) throws Throwable {
        System.out.println("before MyRpcInterceptor2");
        final Object invoke = chain.invoke(id, rpcProtocol);
        System.out.println("after MyRpcInterceptor2");
        return invoke;
    }
}