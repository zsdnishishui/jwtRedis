package com.zsd.service;

import com.zsd.entity.User;
import com.zsd.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;


    @Override
    public String eat() {
        return "1";
    }

    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public List<String> selectAuthList(String username) {
        return userMapper.selectAuthList(username);
    }
}
