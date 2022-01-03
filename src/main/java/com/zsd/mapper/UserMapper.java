package com.zsd.mapper;

import com.zsd.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAll();

    void save(User user);
}
