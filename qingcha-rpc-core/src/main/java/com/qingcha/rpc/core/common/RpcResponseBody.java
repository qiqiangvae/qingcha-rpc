package com.qingcha.rpc.core.common;

/**
 * @author qiqiang
 * @date 2020-11-03 5:29 下午
 */
public class RpcResponseBody<T> {
    private boolean success;
    private T body;
    private String message;
    private String exception;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}