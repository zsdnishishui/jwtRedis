package com.zsd.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zsd.annotation.PreAuth;
import com.zsd.entity.User;
import com.zsd.service.UserService;
import com.zsd.util.JwtHelper;
import com.zsd.util.RedisUtil;
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


	@RequestMapping("/login")
	public Map<String,Object> login(HttpServletRequest request, @RequestBody Map<String,Object> resmap){
		String username = resmap.get("username").toString();
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String, Object> claims = new HashMap<String, Object>();
		User user = userService.selectByUsername(username);
		if (user == null){
			map.put("msg", "用户名或密码错误");
		} else{
			// 查询有哪些权限
			List<String> authList = userService.selectAuthList(username);
			user.setAuthList(authList);
			// 把用户信息放到redis中
			redisUtil.set("username:"+username, JSON.toJSONString(user));
			claims.put("username", username);
			String token=jwtHelper.generateToken(claims);
			map.put("data", token);
			map.put("msg", "success");
			map.put("code", 0);
		}
		return map;
	}

	@PreAuth("121")
	@RequestMapping("/getDate")
	public Map<String,Object> getDate(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("data", "getDate");
		map.put("msg", "success");
		map.put("code", 0);
		return map;
	}
}
