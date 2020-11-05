package com.qingcha.rpc.server.invoke;

import com.qingcha.rpc.core.utils.ClassScanner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiqiang
 * @date 2020-11-03 3:11 下午
 */
public class DefaultMethodPool extends AbstractMethodPool {
    private ConcurrentHashMap<String, InvokeMetaDataInfo> invokeMetaDataInfoMap;
    private String packagePath;

    @Override
    public void refresh() {
        List<Class<?>> classList = ClassScanner.scan(packagePath);
        invokeMetaDataInfoMap = new ConcurrentHashMap<>(classList.size() * 2);
        this.deal(classList,new DefaultInvokeMateInfoBuilder());
    }

    @Override
    protected Map<String, InvokeMetaDataInfo> getInvokeMetaDataInfoMap() {
        return invokeMetaDataInfoMap;
    }

    @Override
    public InvokeMetaDataInfo get(String fullInvokeKey) {
        return invokeMetaDataInfoMap.get(fullInvokeKey);
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }
}