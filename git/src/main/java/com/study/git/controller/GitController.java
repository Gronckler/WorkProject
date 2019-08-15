package com.study.git.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitController {

    @GetMapping("/msg")
    public Object getMsg(){
        System.out.println("git msg!");
        return "hello git!";
    }

}
