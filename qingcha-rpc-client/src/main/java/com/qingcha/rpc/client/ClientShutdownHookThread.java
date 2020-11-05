package com.qingcha.rpc.client;

/**
 * @author qiqiang
 * @date 2020-11-03 10:57 上午
 */
public class ClientShutdownHookThread extends Thread {
    private RpcClientExecutor rpcServerExecutor;

    public ClientShutdownHookThread(RpcClientExecutor rpcClientExecutor) {
        this.rpcServerExecutor = rpcClientExecutor;
    }

    @Override
    public void run() {
        if (rpcServerExecutor != null) {
            rpcServerExecutor.close();
        }
    }
}