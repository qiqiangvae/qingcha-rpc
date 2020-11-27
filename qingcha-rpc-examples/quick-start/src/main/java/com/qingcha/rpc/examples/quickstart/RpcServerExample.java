package com.qingcha.rpc.examples.quickstart;

import com.qingcha.rpc.server.RpcServer;
import com.qingcha.rpc.server.RpcServerConfiguration;
import com.qingcha.rpc.server.interceptor.ServerRpcInterceptorManager;

/**
 * @author qiqiang
 * @date 2020-11-03 12:33 下午
 */
public class RpcServerExample {
    public static void main(String[] args) {
        // 配置需要扫描的服务实现类所在包
        RpcServerConfiguration.configuration().setPackagePath("com.qingcha.rpc.examples.quickstart.service");
        ServerRpcInterceptorManager.addLastInterceptor(MyServerRpcInterceptor1.class);
        ServerRpcInterceptorManager.addLastInterceptor(MyServerRpcInterceptor2.class);
        RpcServer rpcServer = new RpcServer(9900);
        rpcServer.start();
    }
}