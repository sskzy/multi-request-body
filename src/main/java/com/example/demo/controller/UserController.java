package com.example.demo.controller;

import com.example.demo.annotation.MultiRequestBody;
import com.example.demo.domain.Page;
import com.example.demo.domain.User;
import com.example.demo.domain.UserExtends;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : songtc
 * @since : 2023/12/14 11:50
 */
@RestController
public class UserController {

    /**
     * localhost:8080/getPage
     */
    @PostMapping("/getPage")
    public void getInfo(@MultiRequestBody Page page,
                        @MultiRequestBody User user,
                        @MultiRequestBody UserExtends userExtends) {
        System.out.println(page);
        System.out.println(user);
        System.out.println(userExtends);
    }
}
