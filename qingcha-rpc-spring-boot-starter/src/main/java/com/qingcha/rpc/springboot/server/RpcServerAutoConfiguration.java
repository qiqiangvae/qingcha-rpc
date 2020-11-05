package com.qingcha.rpc.springboot.server;

import org.springframework.context.annotation.Bean;

/**
 * @author qiqiang
 * @date 2020-11-04 3:03 下午
 */
public class RpcServerAutoConfiguration {

    @Bean
    public RpcServerSmartLifecycle rpcServerSmartLifecycle() {
        return new RpcServerSmartLifecycle();
    }

    @Bean
    public SpringRpcMethodPoolFactory springRpcMethodPoolFactory() {
        return new SpringRpcMethodPoolFactory();
    }

}