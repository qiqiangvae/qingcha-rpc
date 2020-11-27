package com.qingcha.rpc.server.invoke;

import com.qingcha.rpc.core.common.RpcResponseBody;
import com.qingcha.rpc.core.interceptor.RpcInterceptorChain;
import com.qingcha.rpc.core.interceptor.RpcInterceptorException;
import com.qingcha.rpc.core.protocol.*;
import com.qingcha.rpc.server.interceptor.InvokeInterceptor;
import com.qingcha.rpc.server.interceptor.ServerInterceptor;
import com.qingcha.rpc.server.interceptor.ServerRpcInterceptorManager;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;

/**
 * @author qiqiang
 * @date 2020-11-26 17:27
 */
public class InvokeThread extends Thread {
    private final Channel channel;
    private final InvokeRequest invokeRequest;
    private final ProtocolSerialize protocolSerialize;

    private final RpcInterceptorChain<InvokeRequest> rpcInterceptorChain;


    public InvokeThread(Channel channel, InvokeRequest invokeRequest) {
        this.channel = channel;
        this.invokeRequest = invokeRequest;
        protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();

        rpcInterceptorChain = new RpcInterceptorChain<>();
        try {
            for (Class<? extends ServerInterceptor> interceptorClass : ServerRpcInterceptorManager.getInterceptorClasses()) {
                rpcInterceptorChain.addLastInterceptor(ServerRpcInterceptorManager.getInterceptor(interceptorClass));
            }
            rpcInterceptorChain.addLastInterceptor(new InvokeInterceptor());
        } catch (Exception e) {
            throw new RpcInterceptorException("拦截器创建失败", e);
        }
    }

    @Override
    public void run() {
        RpcProtocolBuilder rpcProtocolBuilder = RpcProtocolBuilder.builder();
        rpcProtocolBuilder.id(invokeRequest.getKey()).type(RequestType.INVOKE_RESPONSE);
        RpcResponseBody rpcResponseBody = new RpcResponseBody();
        try {
            // 通过反射调用
            Object invokeResult = rpcInterceptorChain.invoke(invokeRequest.getKey(), invokeRequest);
            // success
            rpcResponseBody.setSuccess(true);
            // response body
            rpcResponseBody.setBody(invokeResult);
        } catch (Throwable throwable) {
            rpcResponseBody.setSuccess(false);
            // response message
            if (throwable instanceof InvocationTargetException) {
                InvocationTargetException targetException = (InvocationTargetException) throwable;
                rpcResponseBody.setMessage(targetException.getMessage());
                rpcResponseBody.setThrowable(targetException);
            } else {
                rpcResponseBody.setMessage(throwable.getMessage());
                rpcResponseBody.setThrowable(throwable);
            }
        } finally {
            rpcInterceptorChain.clear();
            RpcProtocol protocol = rpcProtocolBuilder.body(protocolSerialize.objToBytes(rpcResponseBody)).build();
            ProtocolSender.send(channel, protocol);
        }
    }
}