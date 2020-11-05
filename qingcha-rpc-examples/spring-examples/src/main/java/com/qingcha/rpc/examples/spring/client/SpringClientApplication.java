package com.qingcha.rpc.examples.spring.client;

import com.alibaba.fastjson.JSON;
import com.qingcha.rpc.examples.spring.service.Book;
import com.qingcha.rpc.examples.spring.service.BookService;
import com.qingcha.rpc.springboot.client.EnableRpcClient;
import com.qingcha.rpc.springboot.client.RpcClientRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author qiqiang
 * @date 2020-11-04 2:53 下午
 */
@SpringBootApplication
@EnableRpcClient
public class SpringClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringClientApplication.class, args);
        BookService bookService = context.getBean(BookService.class);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    List<Book> books = bookService.findByAuthor("金庸");
                    System.out.println(JSON.toJSONString(books));
                }
            });
        }
    }
}