package com.qingcha.rpc.examples.quickstart.service;

/**
 * @author qiqiang
 * @date 2020-11-04 12:34 下午
 */
public class Book {
    private String name;

    public Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}