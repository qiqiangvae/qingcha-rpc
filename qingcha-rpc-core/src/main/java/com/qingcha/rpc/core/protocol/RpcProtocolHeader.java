package com.qingcha.rpc.core.protocol;

/**
 * RpcProtocol 头信息
 *
 * @author qiqiang
 * @date 2020-11-03 12:37 下午
 */
public class RpcProtocolHeader {
    /**
     * 请求id
     */
    private String id;
    /**
     * 版本号
     */
    private String version;
    /**
     * 请求类型
     */
    private int type;

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