package com.qingcha.rpc.client.interceptor;

import com.qingcha.rpc.core.interceptor.RpcInterceptorException;
import com.qingcha.rpc.core.interceptor.ShareInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiqiang
 * @date 2020-11-26 15:41
 */
public class ClientRpcInterceptorManager {
    private static final Logger logger = LoggerFactory.getLogger(ClientRpcInterceptorManager.class);

    private static final LinkedList<Class<? extends ClientInterceptor>> INTERCEPTOR_CLASSES = new LinkedList<>();
    private static final Map<Class<? extends ClientInterceptor>, Object> SHARE_INTERCEPTORS = new ConcurrentHashMap<>();

    public static void addLastInterceptor(Class<? extends ClientInterceptor> clazz) {
        try {
            ShareInterceptor shareInterceptor = clazz.getAnnotation(ShareInterceptor.class);
            INTERCEPTOR_CLASSES.addLast(clazz);
            if (shareInterceptor != null) {
                Constructor<? extends ClientInterceptor> constructor = clazz.getConstructor();
                Object interceptor = constructor.newInstance();
                SHARE_INTERCEPTORS.put(clazz, interceptor);
            }
        } catch (Exception e) {
            throw new RpcInterceptorException("拦截器创建失败", e);
        }
    }

    public static LinkedList<Class<? extends ClientInterceptor>> getInterceptorClasses() {
        return INTERCEPTOR_CLASSES;
    }

    public static <T extends ClientInterceptor> T getInterceptor(Class<T> clazz) throws RpcInterceptorException {
        ShareInterceptor shareInterceptor = clazz.getAnnotation(ShareInterceptor.class);
        try {
            if (shareInterceptor == null) {
                Constructor<T> constructor = clazz.getConstructor();
                return constructor.newInstance();
            } else {
                return (T) SHARE_INTERCEPTORS.get(clazz);
            }
        } catch (Exception e) {
            throw new RpcInterceptorException("拦截器创建失败", e);
        }
    }
}