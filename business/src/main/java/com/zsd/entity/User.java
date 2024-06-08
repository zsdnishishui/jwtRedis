package com.zsd.entity;

import lombok.Data;

import java.util.List;


@Data
public class User {

    private Long id;

    private String username;

    private String password;

    private Integer age;
    /**
     * 有哪些权限
     */
    private List<String> authList;
}
