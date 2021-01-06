package com.qingcha.rpc.core.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * fastjson 序列化工具
 *
 * @author qiqiang
 * @date 2020-11-03 5:51 下午
 */
public class FastJsonProtocolSerialize implements ProtocolSerialize {
    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    @Override
    public byte[] objToBytes(Object obj) {
        try {
            return JSON.toJSONBytes(obj, SerializerFeature.EMPTY);
        } catch (Exception e) {
            throw new ProtocolSerializeException("fastjson objToBytes 序列化异常！", e);
        }
    }

    @Override
    public <T> T bytesToObj(byte[] source, Class<T> clazz) {
        try {
            return JSON.parseObject(source, clazz);
        } catch (Exception e) {
            throw new ProtocolSerializeException("fastjson bytesToObj 反序列化异常！", e);
        }
    }
}