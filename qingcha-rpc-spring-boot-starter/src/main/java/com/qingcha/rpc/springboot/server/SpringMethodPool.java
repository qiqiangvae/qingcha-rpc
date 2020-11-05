package com.qingcha.rpc.springboot.server;

import com.qingcha.rpc.server.invoke.InvokeMetaDataInfo;
import com.qingcha.rpc.server.invoke.AbstractMethodPool;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
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
    private Class<? extends Annotation> serviceAnnotation;

    private ConcurrentHashMap<String, InvokeMetaDataInfo> invokeMetaDataInfoMap;

    public SpringMethodPool(ApplicationContext applicationContext, Class<? extends Annotation> serviceAnnotation) {
        this.applicationContext = applicationContext;
        this.serviceAnnotation = serviceAnnotation;
    }

    public SpringMethodPool(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public InvokeMetaDataInfo get(String fullInvokeKey) {
        return invokeMetaDataInfoMap.get(fullInvokeKey);
    }

    @Override
    public void refresh() {
        // 获取被标记的所有的服务
        if (serviceAnnotation == null) {
            serviceAnnotation = Service.class;
        }
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(serviceAnnotation);
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