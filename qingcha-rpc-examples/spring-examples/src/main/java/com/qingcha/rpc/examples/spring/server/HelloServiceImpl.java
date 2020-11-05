package com.qingcha.rpc.examples.spring.server;

import com.qingcha.rpc.core.common.RpcInvoke;
import com.qingcha.rpc.examples.spring.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author qiqiang
 * @date 2020-11-03 3:33 下午
 */
@RpcInvoke
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        if (name.equals("JonKee9")) {
            throw new RuntimeException("JonKee9 异常测试");
        }
        return "hello qingcha";
    }
}