package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : songtc
 * @since : 2023/12/14 11:36
 */
@Setter
@Getter
public class User {
    private String username;
    private Integer password;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password=" + password +
                '}';
    }
}
