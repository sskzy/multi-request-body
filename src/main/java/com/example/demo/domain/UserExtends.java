package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : songtc
 * @since : 2023/12/14 11:37
 */
@Getter
@Setter
public class UserExtends extends User {
    private String nickname;

    @Override
    public String toString() {
        return "UserExtends{" +
                "nickname='" + nickname + '\'' +
                ", user=" + super.toString() +
                '}';
    }
}
