package com.qingcha.rpc.core.protocol;

public interface RequestType {
    int PING = 0;
    int PONG = 1;
    int INVOKE = 2;
    int INVOKE_RESPONSE = 3;
}
