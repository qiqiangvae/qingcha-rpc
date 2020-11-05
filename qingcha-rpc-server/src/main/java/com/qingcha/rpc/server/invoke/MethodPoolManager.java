package com.qingcha.rpc.server.invoke;

import com.qingcha.rpc.core.protocol.FastJsonProtocolSerialize;
import com.qingcha.rpc.core.protocol.ProtocolSerialize;
import com.qingcha.rpc.server.RpcServerConfiguration;

import java.util.ServiceLoader;

/**
 * 方法池管理器
 *
 * @author qiqiang
 * @date 2020-11-03 6:00 下午
 */
public class MethodPoolManager {
    private static MethodPool methodPool;

    public static void setMethodPool(MethodPool methodPool) {
        MethodPoolManager.methodPool = methodPool;
    }

    public static synchronized MethodPool getMethodPool() {
        if (methodPool == null) {
            // 从 spi 中获取 methodPool
            ServiceLoader<MethodPoolFactory> methodPoolFactories = ServiceLoader.load(MethodPoolFactory.class);
            for (MethodPoolFactory factory : methodPoolFactories) {
                if (factory != null) {
                    methodPool = factory.getMethodPool();
                    break;
                }
            }
            // 如果获取不到，则用默认的
            if (methodPool == null) {
                RpcServerConfiguration configuration = RpcServerConfiguration.configuration();
                methodPool = new DefaultMethodPoolFactory(configuration.getPackagePath()).getMethodPool();
            }
        }
        return methodPool;
    }
}