package com.zsd.setting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
public class RoleSetting implements ApplicationRunner{
	public static Map<String, List<String>> ROLE_AUTH;
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("获取权限");
		 Map<String, List<String>> map=new HashMap<String, List<String>>();
		 
		 map.put("admin", Arrays.asList("/hello","/getdate"));
		 map.put("noAdmin", Arrays.asList("/hello"));
		 ROLE_AUTH=map;
	}

}
