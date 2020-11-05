package com.qingcha.rpc.core.protocol;

import com.qingcha.rpc.core.utils.IdUtils;
import com.qingcha.rpc.core.utils.UsefulUtils;

/**
 * RpcProtocol 构建者
 *
 * @author qiqiang
 * @date 2020-11-05 9:52 上午
 */
public class RpcProtocolBuilder {
    private String id;
    private String version;
    private int type;
    private byte[] body;

    private RpcProtocolBuilder() {
    }

    public static RpcProtocolBuilder builder() {
        return new RpcProtocolBuilder();
    }

    public RpcProtocolBuilder id(String requestId) {
        this.id = requestId;
        return this;
    }

    public RpcProtocolBuilder version(RpcVersion version) {
        this.version = version.name();
        return this;
    }

    public RpcProtocolBuilder type(int requestType) {
        this.type = requestType;
        return this;
    }

    public RpcProtocolBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    public RpcProtocol build() {
        RpcProtocol rpcProtocol = new RpcProtocol();
        RpcProtocolHeader header = new RpcProtocolHeader();
        if (UsefulUtils.isBlack(id)) {
            id = IdUtils.uuid();
        }
        header.setId(id);
        if (UsefulUtils.isBlack(version)) {
            version = CurrentRpcVersion.VERSION.name();
        }
        header.setVersion(version);
        header.setType(type);
        rpcProtocol.setHeader(header);
        rpcProtocol.setBody(body);
        return rpcProtocol;
    }
}