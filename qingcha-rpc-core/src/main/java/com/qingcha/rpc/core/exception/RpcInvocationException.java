package com.qingcha.rpc.core.exception;

/**
 * @author qiqiang
 */
public class RpcInvocationException extends Throwable {
    private static final long serialVersionUID = -100L;

    public RpcInvocationException(Throwable cause) {
        super(cause);
    }
}