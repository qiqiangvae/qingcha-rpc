package com.qingcha.rpc.server.invoke;

import java.lang.reflect.Method;

/**
 * @author qiqiang
 * @date 2020-11-03 4:21 下午
 */
public interface InvokeMateInfoBuilder {

    InvokeMetaDataInfo build(String invokeKey, Method method, Class<?> clazz, Class<?> parentClass);
}