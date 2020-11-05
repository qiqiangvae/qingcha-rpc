package com.qingcha.rpc.core.protocol;

/**
 * 请求类型
 * @author qiqiang
 */
public interface RequestType {
    int PING = 0;
    int PONG = 1;
    int INVOKE = 2;
    int INVOKE_RESPONSE = 3;
}
