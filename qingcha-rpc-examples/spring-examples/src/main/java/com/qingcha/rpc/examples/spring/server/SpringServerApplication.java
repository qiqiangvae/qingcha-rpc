package com.qingcha.rpc.examples.spring.server;

import com.qingcha.rpc.springboot.server.EnableRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qiqiang
 * @date 2020-11-04 2:53 下午
 */
@SpringBootApplication
@EnableRpcServer
public class SpringServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringServerApplication.class, args);
    }
}