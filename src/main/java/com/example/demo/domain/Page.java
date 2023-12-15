package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author : songtc
 * @since : 2023/12/14 11:36
 */
@Getter
@Setter
@ToString
public class Page {
    private Integer current;
    private Integer size;
    private Integer total;
}
