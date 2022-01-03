package com.zsd.controller;


import com.zsd.entity.User;
import com.zsd.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private DataSource dataSource;

    @GetMapping("save")
    public void save(){
        User user = new User();
        user.setId(1000L);
        user.setUsername("121212000");
        userMapper.save(user);
    }

    @GetMapping("getAll")
    public Object getAll() throws SQLException {
//        String sql = "select * from user";
//        Connection conn = dataSource.getConnection();
//        PreparedStatement ps = conn.prepareStatement(sql);
//        ResultSet rs = ps.executeQuery();
//        while(rs.next()){
//
//            System.out.println(rs.getString(2));
//        }
//
//        return null;
        return userMapper.findAll();
    }
}
