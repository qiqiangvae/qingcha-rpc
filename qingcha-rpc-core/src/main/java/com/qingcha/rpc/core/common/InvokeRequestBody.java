package com.qingcha.rpc.core.common;

import java.util.Arrays;

/**
 * 调用请求体
 *
 * @author qiqiang
 * @date 2020-11-03 4:59 下午
 */
public class InvokeRequestBody {
    private String fullInvokeKey;
    private Object[] args;

    public String getFullInvokeKey() {
        return fullInvokeKey;
    }

    public void setFullInvokeKey(String fullInvokeKey) {
        this.fullInvokeKey = fullInvokeKey;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "{" +
                "fullInvokeKey='" + fullInvokeKey + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}