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

    public static ProtocolSerialize getProtocolSerialize() {
        // 如果序列化工具不存在，那么初始化
        if (protocolSerialize == null) {
            init();
        }
        return protocolSerialize;
    }

    /**
     * 初始化的时候先从 SPI 中获取，如果从 SPI 获取不到，则使用默认的 {@link JacksonProtocolSerialize}
     */
    private static synchronized void init() {
        if (protocolSerialize == null) {
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
}