package com.qingcha.rpc.core.protocol;

/**
 * @author qiqiang
 * @date 2020-11-03 5:48 下午
 */
public interface ProtocolSerialize {
    /**
     * 对象转 bytes
     *
     * @param obj 对象
     * @return bytes
     */
    byte[] objToBytes(Object obj);

    /**
     * bytes 转对象
     *
     * @param source bytes
     * @param clazz  类
     * @param <T>    类
     * @return 对象
     */
    <T> T bytesToObj(byte[] source, Class<T> clazz);
}