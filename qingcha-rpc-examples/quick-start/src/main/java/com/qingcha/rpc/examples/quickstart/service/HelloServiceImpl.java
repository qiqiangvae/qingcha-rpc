package com.qingcha.rpc.examples.quickstart.service;

import com.qingcha.rpc.core.common.RpcInvoke;

/**
 *
 * @author qiqiang
 * @date 2020-11-03 3:33 下午
 */
@RpcInvoke
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String name) {
        return "hello qingcha";
    }
}