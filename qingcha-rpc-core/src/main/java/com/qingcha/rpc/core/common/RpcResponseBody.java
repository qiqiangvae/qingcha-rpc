package com.qingcha.rpc.core.common;

/**
 * @author qiqiang
 * @date 2020-11-03 5:29 下午
 */
public class RpcResponseBody<T> {
    private boolean success;
    private T body;
    private String message;
    private Throwable throwable;

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

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}