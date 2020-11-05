package com.qingcha.rpc.core.protocol;

/**
 * @author qiqiang
 * @date 2020-11-05 2:22 下午
 */
public class ProtocolSerializeException extends RuntimeException{
    public ProtocolSerializeException() {
    }

    public ProtocolSerializeException(String message) {
        super(message);
    }

    public ProtocolSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolSerializeException(Throwable cause) {
        super(cause);
    }

    public ProtocolSerializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}