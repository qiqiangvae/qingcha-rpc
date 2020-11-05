package com.qingcha.rpc.server;

import com.qingcha.rpc.server.invoke.InvokeMetaDataInfo;
import com.qingcha.rpc.core.common.InvokeRequestBody;
import com.qingcha.rpc.core.protocol.*;
import com.qingcha.rpc.core.utils.LoggerUtils;
import com.qingcha.rpc.server.invoke.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiqiang
 * @date 2020-11-03 2:35 下午
 */
@ChannelHandler.Sharable
public class RpcServerRequestHandler extends SimpleChannelInboundHandler<RpcProtocol> {
    private final Logger logger = LoggerFactory.getLogger(RpcServerRequestHandler.class);
    private MethodPool methodPool;
    private InvokeProcessor invokeProcessor;
    private final ProtocolSerialize protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();

    public RpcServerRequestHandler() {
        init();
    }

    private void init() {
        methodPool = MethodPoolManager.getMethodPool();
        invokeProcessor = new InvokeProcessor();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol msg) throws Exception {
        RpcProtocolHeader header = msg.getHeader();
        LoggerUtils.debug(logger, () -> logger.debug("协议版本[{}]，请求类型[{}]，请求Id[{}]", header.getVersion(), header.getType(), header.getId()));
        byte[] body = msg.getBody();
        switch (header.getType()) {
            case RequestType.PING:
                LoggerUtils.info(logger, () -> logger.info("收到客户端[{}]的 PING 消息", ctx.channel().remoteAddress()));
                RpcProtocol pong = RpcProtocolBuilder.builder().type(RequestType.PONG).build();
                ProtocolSender.send(ctx.channel(), pong);
                RpcProtocol ping = RpcProtocolBuilder.builder().type(RequestType.PING).build();
                ProtocolSender.send(ctx.channel(), ping);
                break;
            case RequestType.PONG:
                LoggerUtils.info(logger, () -> logger.info("收到客户端[{}]的 PONG 消息", ctx.channel().remoteAddress()));
                break;
            case RequestType.INVOKE:
                InvokeRequestBody invokeRequestBody = protocolSerialize.bytesToObj(body, InvokeRequestBody.class);
                LoggerUtils.debug(logger, () -> logger.debug("协议请求体：[{}]", invokeRequestBody));
                InvokeMetaDataInfo invokeMetaDataInfo = methodPool.get(invokeRequestBody.getFullInvokeKey());
                if (invokeMetaDataInfo != null) {
                    invokeProcessor.invoke(header.getId(), ctx.channel(), invokeMetaDataInfo, invokeRequestBody.getArgs());
                } else {
                    LoggerUtils.warn(logger, () -> logger.warn("[{}]不存在！", invokeRequestBody.getFullInvokeKey()));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LoggerUtils.debug(logger, () -> logger.debug("channelActive:" + ctx.channel().remoteAddress()));
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LoggerUtils.debug(logger, () -> logger.debug("channelInactive:" + ctx.channel().remoteAddress()));
        super.channelInactive(ctx);
    }
}