package com.qingcha.rpc.examples.quickstart.service;

import java.util.List;

/**
 * @author qiqiang
 * @date 2020-11-04 12:34 下午
 */
public interface BookService {
    List<Book> findByAuthor(String author);
}