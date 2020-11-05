package com.qingcha.rpc.core.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * jackson 序列化工具
 *
 * @author qiqiang
 * @date 2020-11-05 2:16 下午
 */
public class JacksonProtocolSerialize implements ProtocolSerialize {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] objToBytes(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new ProtocolSerializeException("jackson objToBytes 序列化异常！", e);
        }
    }

    @Override
    public <T> T bytesToObj(byte[] source, Class<T> clazz) {
        try {
            return objectMapper.readValue(source, clazz);
        } catch (IOException e) {
            throw new ProtocolSerializeException("jackson bytesToObj 反序列化异常！", e);
        }
    }
}