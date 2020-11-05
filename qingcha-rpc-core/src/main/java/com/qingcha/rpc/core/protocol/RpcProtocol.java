package com.qingcha.rpc.core.protocol;

/**
 * rpc 协议
 *
 * @author qiqiang
 * @date 2020-11-03 11:49 上午
 */
public class RpcProtocol {
    private final int start = ConstantValue.HEAD_START;
    /**
     * header 长度
     */
    private int headerLength;
    /**
     * header
     */
    private RpcProtocolHeader header;
    /**
     * body 长度
     */
    private int bodyLength;
    /**
     * body
     */
    private byte[] body;

    public RpcProtocolHeader getHeader() {
        return header;
    }

    public void setHeader(RpcProtocolHeader header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getStart() {
        return start;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }
}