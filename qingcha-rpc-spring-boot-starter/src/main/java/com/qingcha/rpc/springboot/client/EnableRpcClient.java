package com.qingcha.rpc.springboot.client;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qiqiang
 * @date 2020-11-04 5:41 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({RpcClientRegistrar.class, RpcClientProperties.class})
public @interface EnableRpcClient {
}