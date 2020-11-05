package com.qingcha.rpc.server.invoke;

import com.qingcha.rpc.core.InvokeMateInfoBuilder;
import com.qingcha.rpc.core.InvokeMetaDataInfo;
import com.qingcha.rpc.core.common.RpcInvoke;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author qiqiang
 * @date 2020-11-03 4:36 下午
 */
public abstract class AbstractMethodPool implements MethodPool {

    /**
     * 刷新容器
     */
    abstract public void refresh();

    protected void deal(List<Class<?>> classList, InvokeMateInfoBuilder invokeMateInfoBuilder) {
        for (Class<?> clazz : classList) {
            // 检查有效性
            if (!checkClass(clazz)) {
                continue;
            }
            Method[] methods = clazz.getMethods();
            // 等待处理的 methods
            List<Method> waitToDeal = new ArrayList<>(methods.length);
            RpcInvoke rpcInvoke = clazz.getAnnotation(RpcInvoke.class);
            if (rpcInvoke == null) {
                // 如果类没有 RpcInvoke，则看在方法上有没有标记 RpcInvoke
                for (Method method : methods) {
                    RpcInvoke methodRpcInvoke = method.getAnnotation(RpcInvoke.class);
                    // 如果存在 RpcInvoke 并且 invokeKey 不为空，那么加入方法池中
                    if (methodRpcInvoke != null) {
                        waitToDeal.add(method);
                    }
                }
            } else {
                // 如果在类上有 RpcInvoke 注解，则说明所有方法都可能会被暴露
                Collections.addAll(waitToDeal, methods);
            }
            Map<Method, Class<?>> parentMethodMap = new HashMap<>(clazz.getInterfaces().length);
            for (Class<?> parentClass : clazz.getInterfaces()) {
                for (Method method : parentClass.getMethods()) {
                    parentMethodMap.put(method, parentClass);
                }
            }
            // 处理
            String invokeKey;
            for (Method method : waitToDeal) {
                RpcInvoke methodRpcInvoke = method.getAnnotation(RpcInvoke.class);
                if (methodRpcInvoke == null) {
                    invokeKey = method.getName();
                } else {
                    invokeKey = methodRpcInvoke.invokeKey();
                }
                if (invokeKey.isEmpty()) {
                    invokeKey = method.getName();
                }
                for (Class<?> parentClass : clazz.getInterfaces()) {
                    // 找到该方法继承或者实现的父类
                    try {
                        parentClass.getMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException e) {
                        continue;
                    }
                    String fullInvokeKey = parentClass.getName() + "@" + invokeKey;
                    getInvokeMetaDataInfoMap().put(fullInvokeKey, invokeMateInfoBuilder.build(invokeKey, method, clazz, parentClass));
                }
            }
        }
    }

    /**
     * 获取容器
     *
     * @return 容器
     */
    abstract protected Map<String, InvokeMetaDataInfo> getInvokeMetaDataInfoMap();

    protected boolean checkClass(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            return false;
        }
        return true;
    }
}