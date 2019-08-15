package com.study.git.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Git2Controller {

    @GetMapping("/git2")
    public Object getMsg() {
        System.out.println("git msg!");
        return "hello git!";
    }

    @GetMapping("/git")
    public Object getMsg2() {
        System.out.println("git msg2!");
        return "hello git2!";
    }

}
