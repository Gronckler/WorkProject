package com.example.architecture.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.example.architecture.pojo.User;
import com.example.architecture.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private ITestService testService;

    @GetMapping("/test")
    public String testMethod(){
        System.out.println("---------");
        int count = testService.test();
        System.out.println("count="+count);
        return "count="+count;
    }


    @PostMapping("/add")
    public String addUser(@RequestBody User user){
        String msg = testService.addUser(user);
        return msg;
    }

    @PostMapping("/query")
    public Object queryUser(@RequestBody User user){
        List<User> list = testService.queryUser(user);
        list.stream().forEach(System.out::println);
        return list;
    }
}
