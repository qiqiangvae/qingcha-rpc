package com.qingcha.rpc.server.invoke;

import com.qingcha.rpc.core.utils.LoggerUtils;
import com.qingcha.rpc.server.RpcServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * 方法池管理器
 *
 * @author qiqiang
 * @date 2020-11-03 6:00 下午
 */
public class MethodPoolManager {
    private static final Logger logger = LoggerFactory.getLogger(MethodPoolManager.class);
    private static MethodPool methodPool;

    public static void setMethodPool(MethodPool methodPool) {
        MethodPoolManager.methodPool = methodPool;
    }

    public static synchronized MethodPool getMethodPool() {
        if (methodPool == null) {
            init();
        }
        return methodPool;
    }

    private static void init() {
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
        LoggerUtils.info(logger, () -> logger.info("使用的 methodPool 为[{}]", methodPool.getClass().getName()));
    }
}