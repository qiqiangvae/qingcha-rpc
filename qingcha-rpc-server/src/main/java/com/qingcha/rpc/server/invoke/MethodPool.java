package com.qingcha.rpc.server.invoke;

/**
 * @author qiqiang
 * @date 2020-11-03 3:06 下午
 */
public interface MethodPool {
    /**
     * 根据 fullInvokeKey 获取调用元数据信息
     *
     * @param fullInvokeKey key
     * @return 元数据
     */
    InvokeMetaDataInfo get(String fullInvokeKey);
}