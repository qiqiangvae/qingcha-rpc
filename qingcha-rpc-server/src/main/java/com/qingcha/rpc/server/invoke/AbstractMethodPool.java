package com.qingcha.rpc.server.invoke;

import com.qingcha.rpc.core.common.RpcInvoke;
import com.qingcha.rpc.core.utils.UsefulUtils;

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
            // 处理
            String invokeKey;
            for (Method method : waitToDeal) {
                // 方法校验
                if (!checkMethod(method)) {
                    continue;
                }
                RpcInvoke methodRpcInvoke = method.getAnnotation(RpcInvoke.class);
                if (methodRpcInvoke == null) {
                    invokeKey = method.getName();
                } else {
                    invokeKey = methodRpcInvoke.invokeKey();
                }
                if (UsefulUtils.isBlack(invokeKey)) {
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

    protected boolean checkMethod(Method method) {
        if (method == null) {
            return false;
        }
        // 非 public 方法忽略，抽象方法忽略，静态方法忽略
        int modifiers = method.getModifiers();
        return Modifier.isPublic(modifiers)
                && !Modifier.isAbstract(modifiers)
                && !Modifier.isStatic(modifiers);
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
        // 接口忽略，抽象类忽略
        return !(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()));
    }
}