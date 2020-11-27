package com.qingcha.rpc.server.interceptor;

import com.qingcha.rpc.core.interceptor.RpcInterceptorChain;
import com.qingcha.rpc.server.invoke.InvokeMetaDataInfo;
import com.qingcha.rpc.server.invoke.InvokeRequest;

import java.lang.reflect.Method;

/**
 * @author qiqiang
 * @date 2020-11-26 15:20
 */
public class InvokeInterceptor implements ServerInterceptor {


    @Override
    public Object intercept(String id, InvokeRequest invokeRequest, RpcInterceptorChain<InvokeRequest> chain) throws Throwable {
        final InvokeMetaDataInfo invokeMetaDataInfo = invokeRequest.getInvokeMetaDataInfo();
        final Method method = invokeMetaDataInfo.getMethod();
        final Object instance = invokeMetaDataInfo.getInstance();
        return method.invoke(instance, invokeRequest.getArgs());
    }
}