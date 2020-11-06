package com.qingcha.rpc.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

/**
 * 将 byte 转 rpc 协议
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
        try {
            if (byteBuf.readableBytes() >= ConstantValue.MIN_MSG_LENGTH) {
                // 防止 socket 字节流攻击
                if (byteBuf.readableBytes() > ConstantValue.MAX_MSG_LENGTH) {
                    byteBuf.skipBytes(byteBuf.readableBytes());
                }
                int begin;
                // 在循环中找到协议开始的位置
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
                // 协议包头数据还未到齐，回到协议开始的位置，等待数据到齐
                if (byteBuf.readableBytes() < headerLength) {
                    byteBuf.readerIndex(begin);
                    return;
                }
                // 读取协议包头数据
                byte[] header = new byte[headerLength];
                byteBuf.readBytes(header);
                // 协议内容长度
                int bodyLength = byteBuf.readInt();
                // 协议包内容数据还未到齐，回到协议开始的位置，等待数据到齐
                if (byteBuf.readableBytes() < bodyLength) {
                    byteBuf.readerIndex(begin);
                    return;
                }
                // 读取协议包内容数据
                byte[] body = new byte[bodyLength];
                byteBuf.readBytes(body);
                // 封装协议
                RpcProtocol rpcProtocol = new RpcProtocol();
                rpcProtocol.setHeaderLength(headerLength);
                rpcProtocol.setHeader(protocolSerialize.bytesToObj(header, RpcProtocolHeader.class));
                rpcProtocol.setBodyLength(bodyLength);
                rpcProtocol.setBody(body);
                out.add(rpcProtocol);
            }
        } catch (Exception e) {
            throw new RpcDecoderException("解码异常", e);
        }
    }
}