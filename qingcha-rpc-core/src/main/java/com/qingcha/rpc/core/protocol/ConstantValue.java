package com.qingcha.rpc.core.protocol;

/**
 * @author qiqiang
 * @date 2020-11-03 1:57 下午
 */
public interface ConstantValue {
    /**
     * 头开始标记
     */
    int HEAD_START = 0x77;

    /**
     * 最大请求头长度
     */
    int MAX_HEADER_LENGTH = 512 * 1024;

    /**
     * 最大消息体长度
     */
    int MAX_BODY_LENGTH = 2 * 1024 * 1024;

    /**
     * 最小消息长度
     */
    int MIN_MSG_LENGTH = 4 + 4;

    /**
     * 最大消息长度
     */
    int MAX_MSG_LENGTH = 4 + MAX_HEADER_LENGTH + 4 + MAX_BODY_LENGTH;
}