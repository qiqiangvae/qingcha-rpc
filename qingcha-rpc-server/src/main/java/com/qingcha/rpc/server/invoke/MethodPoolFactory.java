package com.qingcha.rpc.server.invoke;

/**
 * @author qiqiang
 * @date 2020-11-03 3:10 下午
 */
public interface MethodPoolFactory {
    /**
     * 获取方法池
     *
     * @return 方法池
     */
    MethodPool getMethodPool();
}