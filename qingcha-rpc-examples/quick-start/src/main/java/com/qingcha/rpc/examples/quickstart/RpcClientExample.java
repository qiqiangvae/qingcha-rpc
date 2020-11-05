package com.qingcha.rpc.examples.quickstart;

import com.qingcha.rpc.client.RpcClient;
import com.qingcha.rpc.core.common.InvokeRequestBody;
import com.qingcha.rpc.core.protocol.*;
import io.netty.channel.Channel;

/**
 * @author qiqiang
 * @date 2020-11-03 12:34 下午
 */
public class RpcClientExample {
    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient("demo", "localhost", 9900);
        rpcClient.start();
        Channel channel = rpcClient.getChannel();
        ProtocolSerialize protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();
        for (int i = 0; i < 1000; i++) {
            RpcProtocol msg = new RpcProtocol();
            RpcProtocolHeader header = new RpcProtocolHeader();
            header.setVersion(RpcVersion.V1_0.name());
            header.setType(RequestType.INVOKE);
            msg.setHeader(header);
            InvokeRequestBody invokeRequestBody = new InvokeRequestBody();
            invokeRequestBody.setArgs(new Object[]{"JonKee" + i});
            invokeRequestBody.setFullInvokeKey("hello");
            msg.setBody(protocolSerialize.objToBytes(invokeRequestBody));
            channel.writeAndFlush(msg);
            System.out.println("成功发送" + i);
        }
    }
}