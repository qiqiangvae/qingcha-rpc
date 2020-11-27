package com.qingcha.rpc.examples.quickstart;

import com.alibaba.fastjson.JSON;
import com.qingcha.rpc.client.RpcClientConfiguration;
import com.qingcha.rpc.client.interceptor.ClientRpcInterceptorManager;
import com.qingcha.rpc.client.proxy.ProxyProcessor;
import com.qingcha.rpc.examples.quickstart.service.Book;
import com.qingcha.rpc.examples.quickstart.service.BookService;
import com.qingcha.rpc.examples.quickstart.service.HelloService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author qiqiang
 * @date 2020-11-03 12:34 下午
 */
public class RpcClientConcurrencyExample {
    public static void main(String[] args) {
        RpcClientConfiguration.configuration().setHost("localhost");
        RpcClientConfiguration.configuration().setPort(9900);
        RpcClientConfiguration.configuration().setPackagePath("com.qingcha.rpc.examples.quickstart.service");
        ProxyProcessor proxyProcessor = ProxyProcessor.instance();
        ClientRpcInterceptorManager.addLastInterceptor(MyClientRpcInterceptor1.class);
        ClientRpcInterceptorManager.addLastInterceptor(MyClientRpcInterceptor2.class);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        BookService bookService = proxyProcessor.getProxy(BookService.class);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    List<Book> books = bookService.findByAuthor("金庸");
                    System.out.println(JSON.toJSONString(books));
                }
            });
        }
        HelloService helloService = proxyProcessor.getProxy(HelloService.class);
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello("JonKee" + i));
        }
    }
}