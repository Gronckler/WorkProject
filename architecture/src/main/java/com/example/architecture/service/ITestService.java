package com.example.architecture.service;

import com.example.architecture.pojo.User;

import java.util.List;

public interface ITestService {
    int test();

    String addUser(User user);

    List<User> queryUser(User user);
}
