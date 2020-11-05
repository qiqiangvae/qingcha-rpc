package com.qingcha.rpc.server.invoke;

import com.qingcha.rpc.core.InvokeMetaDataInfo;
import com.qingcha.rpc.core.common.RpcResponseBody;
import com.qingcha.rpc.core.protocol.*;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
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
            Object invokeResult = method.invoke(invokeMetaDataInfo.getInstance(), args);
            // success
            rpcResponseBody.setSuccess(true);
            // response body
            rpcResponseBody.setBody(invokeResult);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // success
            rpcResponseBody.setSuccess(true);
            // response message
            rpcResponseBody.setMessage("调用失败！");
        } finally {
            rpcProtocolBuilder.body(protocolSerialize.objToBytes(rpcResponseBody));
            ProtocolSender.send(channel, rpcProtocolBuilder.build());
        }
    }
}