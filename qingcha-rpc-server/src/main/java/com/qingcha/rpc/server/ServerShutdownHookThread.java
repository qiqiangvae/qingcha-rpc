package com.qingcha.rpc.server;

/**
 * @author qiqiang
 * @date 2020-11-03 10:57 上午
 */
public class ServerShutdownHookThread extends Thread {
    private RpcServerExecutor rpcServerExecutor;

    public ServerShutdownHookThread(RpcServerExecutor rpcServerExecutor) {
        this.rpcServerExecutor = rpcServerExecutor;
    }

    @Override
    public void run() {
        if (rpcServerExecutor != null) {
            rpcServerExecutor.close();
        }
    }
}