package com.qingcha.rpc.core.protocol;

import java.util.ServiceLoader;

/**
 * 序列化管理器，可以配置自己的序列化工具
 *
 * @author qiqiang
 * @date 2020-11-03 6:00 下午
 */
public class ProtocolSerializeManager {
    private static ProtocolSerialize protocolSerialize;

    public static void setProtocolSerialize(ProtocolSerialize protocolSerialize) {
        ProtocolSerializeManager.protocolSerialize = protocolSerialize;
    }

    public static synchronized ProtocolSerialize getProtocolSerialize() {
        // 从 SPI 中获取
        if (protocolSerialize == null) {
            ServiceLoader<ProtocolSerialize> serviceLoader = ServiceLoader.load(ProtocolSerialize.class);
            for (ProtocolSerialize serialize : serviceLoader) {
                if (serialize != null) {
                    protocolSerialize = serialize;
                    break;
                }
            }
        }
        // 使用默认的序列化工具
        if (protocolSerialize == null) {
            protocolSerialize = new FastJsonProtocolSerialize();
        }
        return protocolSerialize;
    }
}