package com.qingcha.rpc.server.invoke;

/**
 * @author qiqiang
 * @date 2020-11-26 18:02
 */
public class InvokeRequest {
    private String key;
    private InvokeMetaDataInfo invokeMetaDataInfo;
    private Object[] args;

    public InvokeRequest(String key, InvokeMetaDataInfo invokeMetaDataInfo, Object[] args) {
        this.key = key;
        this.invokeMetaDataInfo = invokeMetaDataInfo;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public InvokeMetaDataInfo getInvokeMetaDataInfo() {
        return invokeMetaDataInfo;
    }

    public void setInvokeMetaDataInfo(InvokeMetaDataInfo invokeMetaDataInfo) {
        this.invokeMetaDataInfo = invokeMetaDataInfo;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}