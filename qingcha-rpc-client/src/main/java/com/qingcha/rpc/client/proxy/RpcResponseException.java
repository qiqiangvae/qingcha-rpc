package com.qingcha.rpc.client.proxy;

/**
 * @author qiqiang
 * @date 2020-11-05 5:13 下午
 */
public class RpcResponseException extends RuntimeException{
    public RpcResponseException() {
    }

    public RpcResponseException(String message) {
        super(message);
    }

    public RpcResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcResponseException(Throwable cause) {
        super(cause);
    }

    public RpcResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}