package com.example.demo.controller;

import com.example.demo.annotation.MultiRequestBody;
import com.example.demo.domain.Complex;
import com.example.demo.domain.Page;
import com.example.demo.domain.User;
import com.example.demo.domain.UserExtends;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : songtc
 * @since : 2023/12/14 11:50
 */
@RestController
public class UserController {

    /**
     * 基本数据类型
     * 基本数据类型包装
     * 基本对象
     * 基本继承对象
     * <p>
     * list集合
     * 基本数据类型包装
     * 基本对象
     * 基本继承对象
     */
    @GetMapping("/getPage")
    public void getInfo(@MultiRequestBody Page page,
                        @MultiRequestBody(value = "user2") User user1,
                        @MultiRequestBody(value = "user1") User user2,
                        @MultiRequestBody UserExtends userExtends,
                        @MultiRequestBody List<Integer> list1,
                        @MultiRequestBody List<User> list2,
                        @MultiRequestBody List<UserExtends> list3) {
        System.out.println(page);
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(userExtends);
        System.out.println(list1);
        System.out.println(list2);
        System.out.println(list3);
    }


    @GetMapping("/getComplex")
    public void getInfo(@MultiRequestBody Page page,
                        @MultiRequestBody Complex complex) {
        System.out.println(page);
        System.out.println(complex);
    }
}
