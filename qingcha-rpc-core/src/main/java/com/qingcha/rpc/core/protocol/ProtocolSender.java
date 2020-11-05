package com.qingcha.rpc.core.protocol;

import io.netty.channel.Channel;

/**
 * 发送协议工具类
 *
 * @author qiqiang
 * @date 2020-11-05 10:09 上午
 */
public class ProtocolSender {
    public static void send(Channel channel, RpcProtocol rpcProtocol) {
        channel.writeAndFlush(rpcProtocol);
    }
}