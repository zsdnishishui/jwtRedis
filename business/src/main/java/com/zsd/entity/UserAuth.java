package com.zsd.entity;

import lombok.Data;

import java.util.List;


@Data
public class UserAuth {

    private Long id;

    private String username;

    private String auth;
}
