package com.qingcha.rpc.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

/**
 * [包头标记] -> [头长度] -> [头内容] -> [body 长度] -> [body 内容]
 *
 * @author qiqiang
 * @date 2020-11-03 11:41 上午
 */
public class ByteToRpcProtocolDecoder extends ByteToMessageDecoder {

    private final ProtocolSerialize protocolSerialize;

    public ByteToRpcProtocolDecoder() {
        protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() >= ConstantValue.MIN_MSG_LENGTH) {
            // 防止 socket 字节流攻击
            if (byteBuf.readableBytes() > ConstantValue.MAX_MSG_LENGTH) {
                byteBuf.skipBytes(byteBuf.readableBytes());
            }
            int begin;
            while (true) {
                // 本次协议包开始的位置
                begin = byteBuf.readerIndex();
                // 标记本次协议包开始的位置
                byteBuf.markReaderIndex();
                if (byteBuf.readInt() == ConstantValue.HEAD_START) {
                    break;
                }
                // 没有读到 HEAD_START，那么就读取下一个字节
                byteBuf.resetReaderIndex();
                byteBuf.readByte();
                if (byteBuf.readableBytes() < ConstantValue.MIN_MSG_LENGTH) {
                    return;
                }
            }
            // 协议包头长度
            int headerLength = byteBuf.readInt();
            // 协议包头数据还未到齐
            if (byteBuf.readableBytes() < headerLength) {
                byteBuf.readerIndex(begin);
            }
            // 读取协议包头数据
            byte[] header = new byte[headerLength];
            byteBuf.readBytes(header);
            // 协议内容长度
            int bodyLength = byteBuf.readInt();
            // 协议包内容数据还未到齐
            if (byteBuf.readableBytes() < bodyLength) {
                byteBuf.readerIndex(begin);
            }
            // 读取协议包内容数据
            byte[] body = new byte[bodyLength];
            byteBuf.readBytes(body);
            RpcProtocol rpcProtocol = new RpcProtocol();
            rpcProtocol.setHeaderLength(headerLength);
            rpcProtocol.setHeader(protocolSerialize.bytesToObj(header, RpcProtocolHeader.class));
            rpcProtocol.setBodyLength(bodyLength);
            rpcProtocol.setBody(body);
            out.add(rpcProtocol);
        }
    }
}