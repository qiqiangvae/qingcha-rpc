package com.qingcha.rpc.server.invoke;

import com.qingcha.rpc.server.RpcServerExecutor;
import io.netty.channel.Channel;


/**
 * 调用处理器
 *
 * @author qiqiang
 * @date 2020-11-03 5:01 下午
 */
public class InvokeProcessor {

    public void invoke(String key, Channel channel, InvokeMetaDataInfo invokeMetaDataInfo, Object... args) {
        InvokeRequest invokeRequest = new InvokeRequest(key, invokeMetaDataInfo, args);
        RpcServerExecutor.getInstance().execute(new InvokeThread(channel, invokeRequest));
    }
}