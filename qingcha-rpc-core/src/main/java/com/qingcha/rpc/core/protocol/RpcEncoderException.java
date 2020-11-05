package com.qingcha.rpc.core.protocol;

/**
 * @author qiqiang
 * @date 2020-11-03 2:05 下午
 */
public class RpcEncoderException extends RuntimeException{
    public RpcEncoderException() {
    }

    public RpcEncoderException(String message) {
        super(message);
    }

    public RpcEncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcEncoderException(Throwable cause) {
        super(cause);
    }

    public RpcEncoderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}