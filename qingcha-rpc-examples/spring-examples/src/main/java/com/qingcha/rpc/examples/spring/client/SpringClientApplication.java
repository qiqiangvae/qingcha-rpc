package com.qingcha.rpc.examples.spring.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingcha.rpc.examples.spring.service.Book;
import com.qingcha.rpc.examples.spring.service.BookService;
import com.qingcha.rpc.springboot.client.EnableRpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

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

    public static void main(String[] args) {
        SpringApplication.run(SpringClientApplication.class, args);
    }

    @Component
    class ApplicationRunnerDemo implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            for (int i = 0; i < 10; i++) {
                executorService.execute(() -> {
                    try {
                        for (int j = 0; j < 10; j++) {
                            List<Book> books = bookService.findByAuthor("金庸");
                            System.out.println(objectMapper.writeValueAsString(books));
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}