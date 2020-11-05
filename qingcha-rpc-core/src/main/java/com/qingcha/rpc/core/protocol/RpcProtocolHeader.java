package com.qingcha.rpc.core.protocol;

/**
 * @author qiqiang
 * @date 2020-11-03 12:37 下午
 */
public class RpcProtocolHeader {
    private String version;
    private int type;
    private String id;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}