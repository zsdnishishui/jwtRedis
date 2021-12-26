package com.zsd.controller;


import com.zsd.entity.User;
import com.zsd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("save")
    public void save(){
        User user = new User();
        user.setId(100L);
        user.setUsername("121212");
        userRepository.save(user);
    }

    @GetMapping("getAll")
    public Object getAll(){
        return userRepository.findAll();
    }
}
