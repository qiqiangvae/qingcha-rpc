package com.qingcha.rpc.client.proxy;

import com.qingcha.rpc.client.RpcClientConfiguration;
import com.qingcha.rpc.core.protocol.ProtocolSerializeManager;
import com.qingcha.rpc.core.utils.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * 代理池管理器
 *
 * @author qiqiang
 * @date 2020-11-04 2:29 下午
 */
public class ProxyPoolManager {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolSerializeManager.class);
    private static ProxyPool proxyPool;

    public static void setProxyPool(ProxyPool proxyPool) {
        ProxyPoolManager.proxyPool = proxyPool;
    }

    public static ProxyPool getProxyPool() {
        if (proxyPool == null) {
            init();
        }
        return proxyPool;
    }

    private static synchronized void init() {
        if (proxyPool == null) {
            // 通过 SPI 机制获取 ProxyPoolFactory，从而获取代理池 proxyPool
            ServiceLoader<ProxyPoolFactory> factories = ServiceLoader.load(ProxyPoolFactory.class);
            for (ProxyPoolFactory factory : factories) {
                if (factory != null) {
                    proxyPool = factory.getProxyPool();
                }
            }
            if (proxyPool == null) {
                RpcClientConfiguration configuration = RpcClientConfiguration.configuration();
                proxyPool = new DefaultProxyPoolFactory(configuration.getPackagePath()).getProxyPool();
            }
            LoggerUtils.info(logger, () -> logger.info("使用的 proxyPool 为[{}]", proxyPool.getClass().getName()));
        }
    }
}