package com.qingcha.rpc.client;

import com.qingcha.rpc.client.proxy.ProxyResponseProcessor;
import com.qingcha.rpc.client.proxy.RpcClientHolder;
import com.qingcha.rpc.core.protocol.ByteToRpcProtocolDecoder;
import com.qingcha.rpc.core.protocol.RpcProtocolToByteEncoder;
import com.qingcha.rpc.core.utils.LoggerUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 每一个接口对应一个 client
 *
 * @author qiqiang
 * @date 2020-11-03 10:48 上午
 */
public class RpcClient {

    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    /**
     * rpc client 线程池
     */
    private RpcClientExecutor rpcClientExecutor;
    /**
     * host 配置
     */
    private String host;
    /**
     * port 配置
     */
    private int port;
    /**
     * client name
     */
    private String name;
    /**
     * 是否启动过
     */
    private volatile boolean started;
    /**
     * rpc client 持有者
     */
    private RpcClientHolder holder;

    private Channel channel;

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition startOk = lock.newCondition();

    public RpcClient(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public RpcClient(String name) {
        this.name = name;
        RpcClientConfiguration configuration = RpcClientConfiguration.configuration();
        this.host = configuration.getHost();
        this.port = configuration.getPort();
    }

    public synchronized void start() {
        rpcClientExecutor = RpcClientExecutor.getInstance();
        rpcClientExecutor.execute(new RpcServerThread());
        lock.lock();
        try {
            startOk.await();
            started = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void setHolder(RpcClientHolder holder) {
        this.holder = holder;
    }

    public boolean isStarted() {
        return started;
    }

    public Channel getChannel() {
        return channel;
    }

    class RpcServerThread implements Runnable {

        @Override
        public void run() {
            if (!started) {
                ProxyResponseProcessor proxyResponseProcessor = new ProxyResponseProcessor(holder);
                RpcClientResponseHandler rpcClientResponseHandler = new RpcClientResponseHandler(proxyResponseProcessor);
                Bootstrap bootstrap = new Bootstrap();
                EventLoopGroup worker = new NioEventLoopGroup();
                bootstrap.group(worker)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                ChannelPipeline pipeline = socketChannel.pipeline();
                                pipeline.addLast(new ByteToRpcProtocolDecoder());
                                pipeline.addLast(new RpcProtocolToByteEncoder());
                                pipeline.addLast(rpcClientResponseHandler);
                            }
                        });
                ChannelFuture channelFuture;
                try {
                    // 添加到 rpcServerExecutor 中，方便 JVM 关闭时停止
                    rpcClientExecutor.addNettyExecutorGroup(worker);
                    channelFuture = bootstrap.connect(host, port).sync();
                    lock.lock();
                    try {
                        channel = channelFuture.channel();
                        startOk.signalAll();
                    } finally {
                        lock.unlock();
                    }
                    LoggerUtils.info(logger, () -> logger.info("[{}]rpc client 启动成功，连接地址[{}:{}]", name, host, port));
                    channel.closeFuture().sync();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    worker.shutdownGracefully();
                }
            } else {
                LoggerUtils.warn(logger, () -> logger.warn("[{}]rpc client 启动成功", name));
            }
        }
    }
}