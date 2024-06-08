package com.zsd.controller;


import com.zsd.entity.User;
import com.zsd.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private DataSource dataSource;

    @GetMapping("save")
    public void save(){
        User user1 = new User();
        user1.setId(1L);
        user1.setAge(3);
        user1.setUsername("111");
        User user2 = new User();
        user2.setId(2L);
        user2.setAge(4);
        user2.setUsername("2222");
        userMapper.save(user1);
        userMapper.save(user2);
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
