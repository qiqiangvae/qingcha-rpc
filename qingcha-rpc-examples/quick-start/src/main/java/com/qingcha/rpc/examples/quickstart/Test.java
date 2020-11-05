package com.qingcha.rpc.examples.quickstart;

import com.qingcha.rpc.examples.quickstart.service.HelloServiceImpl;

import java.lang.reflect.Method;

/**
 * @author qiqiang
 * @date 2020-11-04 9:58 上午
 */
public class Test {
    public static void main(String[] args) {
        Class<HelloServiceImpl> helloServiceClass = HelloServiceImpl.class;
        for (Class<?> anInterface : helloServiceClass.getInterfaces()) {
            Method[] methods = anInterface.getMethods();
            for (Method method : methods) {
                System.out.println(method);
            }
        }
        Method[] methods = helloServiceClass.getMethods();
        for (Method method : methods) {
//            method.getModifiers()
        }
    }
}