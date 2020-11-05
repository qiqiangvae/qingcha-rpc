package com.qingcha.rpc.examples.spring.server;

import com.qingcha.rpc.core.common.RpcInvoke;
import com.qingcha.rpc.examples.spring.service.Book;
import com.qingcha.rpc.examples.spring.service.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 * @date 2020-11-04 12:34 下午
 */
@Service
public class BookServiceImpl implements BookService {
    @Override
    @RpcInvoke
    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        if ("金庸".equals(author)) {
            books.add(new Book("天龙八部"));
            books.add(new Book("射雕英雄转"));
            books.add(new Book("倚天屠龙记"));
        }
        return books;
    }
}