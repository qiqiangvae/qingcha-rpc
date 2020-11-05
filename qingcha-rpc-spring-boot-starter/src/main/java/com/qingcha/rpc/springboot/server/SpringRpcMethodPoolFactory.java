package com.qingcha.rpc.springboot.server;

import com.qingcha.rpc.server.invoke.MethodPool;
import com.qingcha.rpc.server.invoke.MethodPoolFactory;
import com.qingcha.rpc.server.invoke.MethodPoolManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author qiqiang
 * @date 2020-11-04 3:04 下午
 */
public class SpringRpcMethodPoolFactory implements MethodPoolFactory, ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;
    private SpringMethodPool springMethodPool;

    @Override
    public MethodPool getMethodPool() {
        return springMethodPool;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        springMethodPool = new SpringMethodPool(applicationContext);
        springMethodPool.refresh();
        MethodPoolManager.setMethodPool(springMethodPool);
    }
}