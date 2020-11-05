package com.qingcha.rpc.springboot.server;

import com.qingcha.rpc.core.InvokeMetaDataInfo;
import com.qingcha.rpc.core.common.RpcInvoke;
import com.qingcha.rpc.server.invoke.AbstractMethodPool;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author qiqiang
 * @date 2020-11-04 3:06 下午
 */
public class SpringMethodPool extends AbstractMethodPool {
    private final ApplicationContext applicationContext;

    private ConcurrentHashMap<String, InvokeMetaDataInfo> invokeMetaDataInfoMap;

    public SpringMethodPool(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public InvokeMetaDataInfo get(String fullInvokeKey) {
        return invokeMetaDataInfoMap.get(fullInvokeKey);
    }

    @Override
    public void refresh() {
        Map<String, Object> beans = applicationContext.getBeansOfType(Object.class);
        invokeMetaDataInfoMap = new ConcurrentHashMap<>(beans.size());
        List<Class<?>> classList = beans.values().stream().map(Object::getClass).collect(Collectors.toList());
        this.deal(classList, (invokeKey, method, clazz, parentClass) -> {
            InvokeMetaDataInfo invokeMetaDataInfo = new InvokeMetaDataInfo();
            invokeMetaDataInfo.setInvokeKey(invokeKey);
            invokeMetaDataInfo.setMethod(method);
            invokeMetaDataInfo.setClazz(clazz);
            invokeMetaDataInfo.setFullInvokeKey(parentClass.getName() + "@" + invokeKey);
            invokeMetaDataInfo.setInstance(applicationContext.getBean(parentClass));
            return invokeMetaDataInfo;
        });
    }

    @Override
    protected Map<String, InvokeMetaDataInfo> getInvokeMetaDataInfoMap() {
        return invokeMetaDataInfoMap;
    }
}