package com.qingcha.rpc.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * [包头标记] -> [头长度] -> [头内容] -> [body 长度] -> [body 内容]
 *
 * @author qiqiang
 * @date 2020-11-03 11:41 上午
 */
public class RpcProtocolToByteEncoder extends MessageToByteEncoder<RpcProtocol> {

    private final ProtocolSerialize protocolSerialize;

    public RpcProtocolToByteEncoder() {
        protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol rpcRequest, ByteBuf byteBuf) throws Exception {
        RpcProtocolHeader header = rpcRequest.getHeader();
        byte[] headerData = protocolSerialize.objToBytes(header);
        if (headerData.length > ConstantValue.MAX_HEADER_LENGTH) {
            throw new RpcEncoderException(String.format("头信息长度超过允许的最大长度[%d]", ConstantValue.MAX_HEADER_LENGTH));
        }
        byte[] body = rpcRequest.getBody();
        if (body != null && body.length > ConstantValue.MAX_BODY_LENGTH) {
            throw new RpcEncoderException(String.format("消息内容长度超过允许的最大长度[%d]", ConstantValue.MAX_BODY_LENGTH));
        }
        // 写入开始标志
        byteBuf.writeInt(ConstantValue.HEAD_START);
        // 写入头信息
        byteBuf.writeInt(headerData.length);
        byteBuf.writeBytes(headerData);
        // 写入 body 信息
        if (body == null) {
            byteBuf.writeInt(0);
        } else {
            byteBuf.writeInt(body.length);
            byteBuf.writeBytes(body);
        }

    }
}