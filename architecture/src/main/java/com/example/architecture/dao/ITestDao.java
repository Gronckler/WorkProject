package com.example.architecture.dao;

import com.example.architecture.pojo.User;

import java.util.List;

public interface ITestDao {
    int count();

    int insertUser(User user);

    List<User> queryUser(User user);
}
