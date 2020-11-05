package com.qingcha.rpc.client.proxy;

/**
 * 没有该服务异常
 *
 * @author qiqiang
 * @date 2020-11-04 11:02 上午
 */
public class NoSuchServiceException extends RuntimeException {
    public NoSuchServiceException() {
    }

    public NoSuchServiceException(String message) {
        super(message);
    }

    public NoSuchServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchServiceException(Throwable cause) {
        super(cause);
    }

    public NoSuchServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}