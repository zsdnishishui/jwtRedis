package com.zsd.controller;

import com.alibaba.fastjson.JSON;
import com.zsd.annotation.PreAuth;
import com.zsd.entity.User;
import com.zsd.service.UserService;
import com.zsd.util.JwtHelper;
import com.zsd.util.RedisUtil;
import com.zsd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录
     *
     * @param request
     * @param resmap
     * @return
     */
    @RequestMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody Map<String, Object> resmap) {
        String username = resmap.get("username").toString();
        Map<String, Object> claims = new HashMap<String, Object>();
        User user = userService.selectByUsername(username);
        if (user == null) {
            return Result.fail("用户名或密码错误");
        } else {
            // 查询有哪些权限
            List<String> authList = userService.selectAuthList(username);
            user.setAuthList(authList);
            // 把用户信息放到redis中
            redisUtil.set("username:" + username, JSON.toJSONString(user));
            claims.put("username", username);
            String token = jwtHelper.generateToken(claims);
            return Result.success("登录成功", token);
        }
    }

    /**
     * 获取资源的测试接口
     *
     * @return
     */
    @PreAuth("121")
    @RequestMapping("/getDate")
    public Result getDate() {
        return Result.success();
    }
}
