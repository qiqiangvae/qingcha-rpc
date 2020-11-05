package com.qingcha.rpc.core.protocol;

/**
 * Rpc Decoder 异常
 *
 * @author qiqiang
 * @date 2020-11-03 2:05 下午
 */
public class RpcDecoderException extends RuntimeException {
    public RpcDecoderException() {
    }

    public RpcDecoderException(String message) {
        super(message);
    }

    public RpcDecoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcDecoderException(Throwable cause) {
        super(cause);
    }

    public RpcDecoderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}