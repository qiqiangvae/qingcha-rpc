package com.qingcha.rpc.server.interceptor;

import com.qingcha.rpc.core.interceptor.RpcInterceptor;
import com.qingcha.rpc.core.protocol.RpcProtocol;
import com.qingcha.rpc.server.invoke.InvokeRequest;

/**
 * @author qiqiang
 * @date 2020-11-26 17:50
 */
public interface ServerInterceptor extends RpcInterceptor<InvokeRequest> {

}