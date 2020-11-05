package com.qingcha.rpc.server;

import com.qingcha.rpc.core.protocol.ByteToRpcProtocolDecoder;
import com.qingcha.rpc.core.protocol.RpcProtocolToByteEncoder;
import com.qingcha.rpc.core.utils.LoggerUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiqiang
 * @date 2020-11-03 10:48 上午
 */
public class RpcServer {

    private final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private RpcServerExecutor rpcServerExecutor;
    private int port;

    public RpcServer(int port) {
        this.port = port;
    }

    public RpcServer() {
        this.port = RpcServerConfiguration.configuration().getPort();
    }

    public synchronized void start() {
        rpcServerExecutor = RpcServerExecutor.getInstance();
        rpcServerExecutor.execute(new RpcServerThread());
    }

    class RpcServerThread implements Runnable {

        @Override
        public void run() {
            RpcServerRequestHandler rpcServerRequestHandler = new RpcServerRequestHandler();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            EventLoopGroup boss = new NioEventLoopGroup(1);
            EventLoopGroup worker = new NioEventLoopGroup();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ByteToRpcProtocolDecoder());
                            pipeline.addLast(new RpcProtocolToByteEncoder());
                            pipeline.addLast(rpcServerRequestHandler);
                        }
                    });
            ChannelFuture channelFuture;
            try {
                // 添加到 rpcServerExecutor 中，方便 JVM 关闭时停止
                rpcServerExecutor.addNettyExecutorGroup(boss);
                rpcServerExecutor.addNettyExecutorGroup(worker);
                channelFuture = serverBootstrap.bind(port).sync();
                LoggerUtils.info(logger, () -> logger.info("rpc server 启动成功，端口[{}]", port));
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            } finally {
                boss.shutdownGracefully();
                worker.shutdownGracefully();
            }
        }
    }
}