package com.qingcha.rpc.client.proxy;

import com.qingcha.rpc.client.RpcClient;
import com.qingcha.rpc.core.common.InvokeRequestBody;
import com.qingcha.rpc.core.protocol.*;
import com.qingcha.rpc.core.utils.IdUtils;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JDK 动态代理 InvocationHandler
 *
 * @author qiqiang
 * @date 2020-11-04 9:44 上午
 */
public class ProxyInvocationHandler implements InvocationHandler {
    private final RpcClientHolder holder;
    private final ProtocolSerialize protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();
    private static final List<String> ignoreMethod = Stream.of(Object.class.getMethods()).map(Method::getName).collect(Collectors.toList());

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (ignoreMethod.contains(methodName)) {
            return method.invoke(proxy, args);
        }
        RpcClient rpcClient = holder.getRpcClient();
        // 懒加载模式，如果没有启动，则现在开始启动
        if (!rpcClient.isStarted()) {
            rpcClient.start();
        }
        Class<?> clazz = holder.getClazz();
        Channel channel = rpcClient.getChannel();
        String id = IdUtils.uuid();
        InvokeRequestBody invokeRequestBody = new InvokeRequestBody();
        invokeRequestBody.setFullInvokeKey(clazz.getName() + "@" + methodName);
        invokeRequestBody.setArgs(args);
        byte[] body = protocolSerialize.objToBytes(invokeRequestBody);
        // 封装 protocol
        RpcProtocol rpcProtocol = RpcProtocolBuilder.builder()
                .id(id).type(RequestType.INVOKE).body(body).build();
        // 写入 protocol
        ProtocolSender.send(channel, rpcProtocol);
        return holder.get(id);
    }

    public ProxyInvocationHandler(RpcClientHolder holder) {
        this.holder = holder;
    }
}