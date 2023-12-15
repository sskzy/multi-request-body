package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author : songtc
 * @since : 2023/12/14 15:59
 */
@Setter
@Getter
public class Complex {
    List<User> userList;
    List<UserExtends> userExtendsList;
    Map<Integer, Map<User, UserExtends>> map;

    @Override
    public String toString() {
        return "Complex{" +
                "userList=" + userList +
                ", userExtendsList=" + userExtendsList +
                ", map=" + map +
                '}';
    }
}
