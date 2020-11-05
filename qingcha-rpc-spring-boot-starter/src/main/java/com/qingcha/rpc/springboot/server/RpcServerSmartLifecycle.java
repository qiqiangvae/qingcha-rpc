package com.qingcha.rpc.springboot.server;

import com.qingcha.rpc.server.RpcServer;
import com.qingcha.rpc.server.RpcServerExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;

/**
 * @author qiqiang
 * @date 2020-11-04 3:24 下午
 */
public class RpcServerSmartLifecycle implements SmartLifecycle {
    private boolean isRunning = false;
    @Value("${qingcha.rpc.server.port}")
    private int port;

    @Override
    public void start() {
        RpcServer rpcServer = new RpcServer(port);
        rpcServer.start();
        isRunning = true;
    }

    @Override
    public void stop() {
        RpcServerExecutor.getInstance().close();
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }
}