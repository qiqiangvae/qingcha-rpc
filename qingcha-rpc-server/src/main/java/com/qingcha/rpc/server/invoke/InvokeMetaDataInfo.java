package com.qingcha.rpc.server.invoke;

import java.lang.reflect.Method;

/**
 * 调用相关元数据
 *
 * @author qiqiang
 * @date 2020-11-03 3:06 下午
 */
public class InvokeMetaDataInfo {
    private String invokeKey;
    /**
     * 调用key，用 classname+@+invokeKey
     */
    private String fullInvokeKey;
    private Method method;
    private Class<?> clazz;
    private Object instance;

    public String getInvokeKey() {
        return invokeKey;
    }

    public void setInvokeKey(String invokeKey) {
        this.invokeKey = invokeKey;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getFullInvokeKey() {
        return fullInvokeKey;
    }

    public void setFullInvokeKey(String fullInvokeKey) {
        this.fullInvokeKey = fullInvokeKey;
    }
}