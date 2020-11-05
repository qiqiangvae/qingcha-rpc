package com.qingcha.rpc.client;

import com.qingcha.rpc.client.proxy.ProxyResponseProcessor;
import com.qingcha.rpc.core.common.RpcResponseBody;
import com.qingcha.rpc.core.protocol.*;
import com.qingcha.rpc.core.utils.IdUtils;
import com.qingcha.rpc.core.utils.LoggerUtils;
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
public class RpcClientResponseHandler extends SimpleChannelInboundHandler<RpcProtocol> {
    private final Logger logger = LoggerFactory.getLogger(RpcClientResponseHandler.class);
    private final ProtocolSerialize protocolSerialize = ProtocolSerializeManager.getProtocolSerialize();
    private final ProxyResponseProcessor proxyResponseProcessor;

    public RpcClientResponseHandler(ProxyResponseProcessor proxyResponseProcessor) {
        this.proxyResponseProcessor = proxyResponseProcessor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol msg) throws Exception {
        RpcProtocolHeader header = msg.getHeader();
        LoggerUtils.debug(logger, () -> logger.debug("协议版本[{}],请求类型[{}]", header.getVersion(), header.getType()));
        byte[] body = msg.getBody();
        switch (header.getType()) {
            case RequestType.PING:
                LoggerUtils.info(logger, () -> logger.info("收到服务端的 PING 消息"));
                RpcProtocol pong = RpcProtocolBuilder.builder().type(RequestType.PONG).build();
                ProtocolSender.send(ctx.channel(), pong);
                break;
            case RequestType.PONG:
                LoggerUtils.info(logger, () -> logger.info("收到服务端的 PONG 消息"));
                break;
            case RequestType.INVOKE_RESPONSE:
                RpcResponseBody rpcResponseBody = protocolSerialize.bytesToObj(body, RpcResponseBody.class);
                proxyResponseProcessor.handleResponse(header.getId(), rpcResponseBody);
                break;
            default:
                break;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LoggerUtils.debug(logger, () -> logger.debug("channelActive:" + ctx.channel().remoteAddress()));
        super.channelActive(ctx);
        RpcProtocol ping = RpcProtocolBuilder.builder().type(RequestType.PING).build();
        ProtocolSender.send(ctx.channel(), ping);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LoggerUtils.debug(logger, () -> logger.debug("channelInactive:" + ctx.channel().remoteAddress()));
        super.channelInactive(ctx);
    }
}