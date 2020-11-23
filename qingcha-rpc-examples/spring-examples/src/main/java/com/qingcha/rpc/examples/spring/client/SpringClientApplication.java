package com.qingcha.rpc.examples.spring.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingcha.rpc.examples.spring.service.Book;
import com.qingcha.rpc.examples.spring.service.BookService;
import com.qingcha.rpc.examples.spring.service.HelloService;
import com.qingcha.rpc.springboot.client.EnableRpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

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
    @Autowired
    private BookService bookService;
    @Autowired
    private HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(SpringClientApplication.class, args);
    }

    @EventListener
    public void onStart(ApplicationReadyEvent applicationReadyEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 50; i++) {
            executorService.execute(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        List<Book> books = bookService.findByAuthor("金庸");
                        System.out.println(objectMapper.writeValueAsString(books));
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println(helloService.hello("JonKee" + i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}