package com.qingcha.rpc.client.proxy;

import com.qingcha.rpc.client.RpcClient;
import com.qingcha.rpc.client.interceptor.ClientInterceptor;
import com.qingcha.rpc.client.interceptor.ClientRpcInterceptorManager;
import com.qingcha.rpc.client.interceptor.ProxySendProtocolInterceptor;
import com.qingcha.rpc.core.common.InvokeRequestBody;
import com.qingcha.rpc.core.interceptor.RpcInterceptor;
import com.qingcha.rpc.core.interceptor.RpcInterceptorChain;
import com.qingcha.rpc.core.interceptor.RpcInterceptorException;
import com.qingcha.rpc.core.protocol.*;
import com.qingcha.rpc.core.utils.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK 动态代理 InvocationHandler
 *
 * @author qiqiang
 * @date 2020-11-04 9:44 上午
 */
public class ProxyInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProxyInvocationHandler.class);
    private final RpcClientHolder holder;
    private final ProtocolSerialize protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();
    private final RpcInterceptorChain<RpcProtocol> rpcInterceptorChain;

    public ProxyInvocationHandler(RpcClientHolder holder) {
        this.holder = holder;
        rpcInterceptorChain = new RpcInterceptorChain<>();
        try {
            for (Class<? extends ClientInterceptor> interceptorClass : ClientRpcInterceptorManager.getInterceptorClasses()) {
                rpcInterceptorChain.addLastInterceptor(ClientRpcInterceptorManager.getInterceptor(interceptorClass));
            }
            rpcInterceptorChain.addLastInterceptor(new ProxySendProtocolInterceptor(this.holder));
        } catch (Exception e) {
            throw new RpcInterceptorException("拦截器创建失败", e);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcClient rpcClient = holder.getRpcClient();
        String methodName = method.getName();
        // 因为代理的是一个接口，没有具体的实现类，所以有一些方法无法处理
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        // 懒加载模式，如果没有启动，则现在开始启动
        if (!rpcClient.isStarted()) {
            rpcClient.start();
        }
        Class<?> clazz = holder.getClazz();
        String id = IdUtils.uuid();
        InvokeRequestBody invokeRequestBody = new InvokeRequestBody();
        invokeRequestBody.setFullInvokeKey(clazz.getName() + "@" + methodName);
        invokeRequestBody.setArgs(args);
        byte[] body = protocolSerialize.objToBytes(invokeRequestBody);
        // 封装 protocol
        RpcProtocol rpcProtocol = RpcProtocolBuilder.builder()
                .id(id).type(RequestType.INVOKE).body(body).build();
        // 写入 protocol
        try {
            return rpcInterceptorChain.invoke(id, rpcProtocol);
        } finally {
            rpcInterceptorChain.clear();
        }
    }

}