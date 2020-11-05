package com.qingcha.rpc.server.invoke;

/**
 * @author qiqiang
 * @date 2020-11-03 3:11 下午
 */
public class DefaultMethodPoolFactory implements MethodPoolFactory {
    private String packagePath;

    public DefaultMethodPoolFactory(String packagePath) {
        this.packagePath = packagePath;
    }

    @Override
    public MethodPool getMethodPool() {
        DefaultMethodPool defaultMethodPool = new DefaultMethodPool();
        defaultMethodPool.setPackagePath(packagePath);
        defaultMethodPool.refresh();
        return defaultMethodPool;
    }
}