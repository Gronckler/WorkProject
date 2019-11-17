package com.example.architecture.service.impl;

import com.example.architecture.dao.ITestDao;
import com.example.architecture.pojo.User;
import com.example.architecture.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ITestServiceImpl implements ITestService {

    @Autowired
    private ITestDao testDao;

    @Override
    public int test() {
        return testDao.count();
    }

    @Override
    public String addUser(User user) {

        int count = testDao.insertUser(user);
        if (count > 0) {
            return "success !";
        } else {
            return "fail !";
        }
    }

    @Override
    public List<User> queryUser(User user) {
        return testDao.queryUser(user);
    }
}
