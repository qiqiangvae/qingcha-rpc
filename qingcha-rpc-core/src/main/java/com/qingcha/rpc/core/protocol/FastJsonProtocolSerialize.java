package com.qingcha.rpc.core.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author qiqiang
 * @date 2020-11-03 5:51 下午
 */
public class FastJsonProtocolSerialize implements ProtocolSerialize {
    @Override
    public byte[] objToBytes(Object obj) {
        return JSON.toJSONBytes(obj, SerializerFeature.EMPTY);
    }

    @Override
    public <T> T bytesToObj(byte[] source, Class<T> clazz) {
        return JSON.parseObject(source, clazz);
    }
}