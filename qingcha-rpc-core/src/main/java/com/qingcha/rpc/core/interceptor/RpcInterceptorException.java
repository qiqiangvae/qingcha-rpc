package com.qingcha.rpc.core.interceptor;

/**
 * @author qiqiang
 * @date 2020-11-26 15:54
 */
public class RpcInterceptorException extends RuntimeException{
    public RpcInterceptorException() {
    }

    public RpcInterceptorException(String message) {
        super(message);
    }

    public RpcInterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcInterceptorException(Throwable cause) {
        super(cause);
    }

    public RpcInterceptorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}