package com.qingcha.rpc.client.interceptor;

import com.qingcha.rpc.client.RpcClient;
import com.qingcha.rpc.client.proxy.RpcClientHolder;
import com.qingcha.rpc.core.interceptor.RpcInterceptorChain;
import com.qingcha.rpc.core.protocol.ProtocolSender;
import com.qingcha.rpc.core.protocol.RpcProtocol;

/**
 * @author qiqiang
 * @date 2020-11-26 15:20
 */
public class ProxySendProtocolInterceptor implements ClientInterceptor {

    private final RpcClientHolder holder;

    public ProxySendProtocolInterceptor(RpcClientHolder holder) {
        this.holder = holder;
    }

    @Override
    public Object intercept(String id, RpcProtocol rpcProtocol, RpcInterceptorChain<RpcProtocol> chain) {
        final RpcClient rpcClient = holder.getRpcClient();
        ProtocolSender.send(rpcClient.getChannel(), rpcProtocol);
        return holder.get(id);
    }
}