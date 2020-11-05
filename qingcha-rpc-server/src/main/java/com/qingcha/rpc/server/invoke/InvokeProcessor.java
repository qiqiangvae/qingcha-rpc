package com.qingcha.rpc.server.invoke;

import com.qingcha.rpc.core.common.RpcResponseBody;
import com.qingcha.rpc.core.protocol.*;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 调用处理器
 *
 * @author qiqiang
 * @date 2020-11-03 5:01 下午
 */
public class InvokeProcessor {
    private final ProtocolSerialize protocolSerialize;

    public InvokeProcessor() {
        protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();
    }

    public void invoke(String key, Channel channel, InvokeMetaDataInfo invokeMetaDataInfo, Object... args) {
        Method method = invokeMetaDataInfo.getMethod();
        RpcProtocolBuilder rpcProtocolBuilder = RpcProtocolBuilder.builder();
        rpcProtocolBuilder.id(key).type(RequestType.INVOKE_RESPONSE);
        RpcResponseBody rpcResponseBody = new RpcResponseBody();
        try {
            // 通过反射调用
            Object invokeResult = method.invoke(invokeMetaDataInfo.getInstance(), args);
            // success
            rpcResponseBody.setSuccess(true);
            // response body
            rpcResponseBody.setBody(invokeResult);
        } catch (IllegalAccessException e) {
            // success
            rpcResponseBody.setSuccess(false);
            // response message
            rpcResponseBody.setMessage(e.getMessage());
            rpcResponseBody.setThrowable(e);
        } catch (InvocationTargetException e) {
            // success
            rpcResponseBody.setSuccess(false);
            // response message
            Throwable targetException = e.getTargetException();
            rpcResponseBody.setMessage(targetException.getMessage());
            rpcResponseBody.setThrowable(targetException);
        } finally {
            RpcProtocol protocol = rpcProtocolBuilder.body(protocolSerialize.objToBytes(rpcResponseBody)).build();
            ProtocolSender.send(channel, protocol);
        }
    }
}