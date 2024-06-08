package com.zsd.service;

import com.zsd.entity.User;

import java.util.List;

public interface UserService {
    String eat();

    User selectByUsername(String username);

    List<String> selectAuthList(String username);
}
