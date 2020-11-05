package com.qingcha.rpc.server.invoke;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 默认
 * @author qiqiang
 * @date 2020-11-03 4:21 下午
 */
public class DefaultInvokeMateInfoBuilder implements InvokeMateInfoBuilder{

    @Override
    public InvokeMetaDataInfo build(String invokeKey, Method method, Class<?> clazz, Class<?> parentClass) {
        InvokeMetaDataInfo invokeMetaDataInfo = new InvokeMetaDataInfo();
        invokeMetaDataInfo.setInvokeKey(invokeKey);
        invokeMetaDataInfo.setMethod(method);
        invokeMetaDataInfo.setClazz(clazz);
        invokeMetaDataInfo.setFullInvokeKey(parentClass.getName() + "@" + invokeKey);
        try {
            Constructor<?> constructor = clazz.getConstructor();
            invokeMetaDataInfo.setInstance(constructor.newInstance());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return invokeMetaDataInfo;
    }

    public static InvokeMetaDataInfo buildForClient(String invokeKey, Method method, Class<?> clazz) {
        InvokeMetaDataInfo invokeMetaDataInfo = new InvokeMetaDataInfo();
        invokeMetaDataInfo.setInvokeKey(invokeKey);
        invokeMetaDataInfo.setMethod(method);
        invokeMetaDataInfo.setClazz(clazz);
        return invokeMetaDataInfo;
    }
}