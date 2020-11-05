package com.qingcha.rpc.examples.quickstart.service;

import com.qingcha.rpc.core.common.RpcInvoke;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 * @date 2020-11-04 12:34 下午
 */
public class BookServiceImpl implements BookService {
    @Override
    @RpcInvoke
    public List<Book> findByAuthor(String author) {
        return new ArrayList<Book>() {{
            add(new Book("天龙八部"));
            add(new Book("射雕英雄转"));
            add(new Book("倚天屠龙记"));
        }};
    }
}