package com.qingcha.rpc.core.protocol;

import com.qingcha.rpc.core.utils.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * 序列化管理器，可以配置自己的序列化工具
 *
 * @author qiqiang
 * @date 2020-11-03 6:00 下午
 */
public class ProtocolSerializeManager {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolSerializeManager.class);
    private static ProtocolSerialize protocolSerialize;

    public static void setProtocolSerialize(ProtocolSerialize protocolSerialize) {
        ProtocolSerializeManager.protocolSerialize = protocolSerialize;
    }

    public static synchronized ProtocolSerialize getProtocolSerialize() {
        // 从 SPI 中获取
        if (protocolSerialize == null) {
            init();
        }
        return protocolSerialize;
    }

    private static void init() {
        ServiceLoader<ProtocolSerialize> serviceLoader = ServiceLoader.load(ProtocolSerialize.class);
        for (ProtocolSerialize serialize : serviceLoader) {
            if (serialize != null) {
                protocolSerialize = serialize;
                break;
            }
        }
        // 使用默认的序列化工具
        if (protocolSerialize == null) {
            protocolSerialize = new JacksonProtocolSerialize();
        }
        LoggerUtils.info(logger, () -> logger.info("使用的 protocolSerialize 为[{}]", protocolSerialize.getClass().getName()));
    }
}