package com.rao.study.shiro.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("/user/query")
    public void queryUser(){
        System.out.println();
    }
}
