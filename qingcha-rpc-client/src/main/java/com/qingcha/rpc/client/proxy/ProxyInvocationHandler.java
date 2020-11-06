package com.qingcha.rpc.client.proxy;

import com.qingcha.rpc.client.RpcClient;
import com.qingcha.rpc.core.common.InvokeRequestBody;
import com.qingcha.rpc.core.protocol.*;
import com.qingcha.rpc.core.utils.IdUtils;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * JDK 动态代理 InvocationHandler
 *
 * @author qiqiang
 * @date 2020-11-04 9:44 上午
 */
public class ProxyInvocationHandler implements InvocationHandler {
    private final RpcClientHolder holder;
    private final ProtocolSerialize protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();
    private final static String TO_STRING = "toString";
    private final static String EQUALS = "equals";
    private final static String HASH_CODE = "hashCode";

    public ProxyInvocationHandler(RpcClientHolder holder) {
        this.holder = holder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcClient rpcClient = holder.getRpcClient();
        String methodName = method.getName();
        // 因为代理的是一个接口，没有具体的实现类，所以有一些方法无法处理
        if (TO_STRING.equals(methodName)) {
            return holder.getClazz().getName() + "@Proxy";
        }
        if (EQUALS.equals(methodName)) {
            return this.equals(args[0]);
        }
        if (HASH_CODE.equals(methodName)) {
            return this.hashCode();
        }
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

}