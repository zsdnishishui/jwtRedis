package com.zsd.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zsd.util.JwtHelper;
@RestController
@RequestMapping("/")
public class LoginController {
	@Autowired
    private JwtHelper jwtHelper;
	@RequestMapping("/login")
	public Map<String,Object> login(HttpServletRequest request, @RequestBody Map<String,Object> resmap){
		String username = resmap.get("username").toString();
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String, Object> claims = new HashMap<String, Object>();
		if ("001".equals(username)||"002".equals(username)) {
			claims.put("username", username);
			String token=jwtHelper.generateToken(claims);
			map.put("token", token);
			map.put("res", "success");
		}else{
			map.put("res", "error");
		}
		
		
		return map;
	}
	@RequestMapping("/getdate")
	public Map<String,String> getdate(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("data", "getdate");
		map.put("res", "success");
		return map;
	}
	@RequestMapping("/renewal")
	public Map<String,String> renewal(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		DecodedJWT decodedJWT = jwtHelper.verifyToken(request,true);
		if (decodedJWT != null) {
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("username", decodedJWT.getClaim("username").asString());
			String token=jwtHelper.generateToken(claims);
			map.put("token", token);
			map.put("res", "success");
		}else{

		}

		return map;
	}
	
	
	@RequestMapping("/go")
	public void go(){
		System.out.println("+++++++++++++go");
	}
}
