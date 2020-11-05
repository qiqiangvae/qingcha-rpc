package com.qingcha.rpc.springboot.client;

/**
 * @author qiqiang
 * @date 2020-11-05 10:52 上午
 */
public class SpringRpcClientConfigurationException extends RuntimeException{
    public SpringRpcClientConfigurationException() {
    }

    public SpringRpcClientConfigurationException(String message) {
        super(message);
    }

    public SpringRpcClientConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpringRpcClientConfigurationException(Throwable cause) {
        super(cause);
    }

    public SpringRpcClientConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}