package com.zsd.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zsd.util.JwtHelper;
import com.zsd.util.RedisUtil;

/**
 * 登录验证过滤器
 */
public class LoginFilter implements Filter {
	private JwtHelper jwtHelper;
    private List<String> urls = null;
    private RedisUtil redisUtil;
    private static final org.springframework.util.PathMatcher pathMatcher = new AntPathMatcher();
    public LoginFilter(JwtHelper jwtHelper, String[] authorisedUrls,RedisUtil redisUtil) {
        this.jwtHelper = jwtHelper;
        this.redisUtil = redisUtil;
        urls = Arrays.asList(authorisedUrls);
    }
	

	/**
	 * 初始化
	 */
	public void init(FilterConfig fc) throws ServletException {
		//FileUtil.createDir("d:/FH/topic/");
	}
	
	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		 
		System.out.println("+++++++++++++++"+request.getRequestURI());
		String uri = request.getRequestURI();
		//"OPTIONS" 是为了放过检测跨域的请求
		if ("/login".equals(uri)||"OPTIONS".equals(request.getMethod())) {
			chain.doFilter(req, res); // 调用下一过滤器
		}else{
			for (String url : urls) {
	            if (pathMatcher.match(url, uri)) {
	            	//判断是否为ajax请求，默认不是  
	                boolean isAjaxRequest = false;  
	                if(request.getHeader("x-requested-with")!=null && request.getHeader("x-requested-with").equals("XMLHttpRequest")){  
	                    isAjaxRequest = true;  
	                }
	            	DecodedJWT decodedJWT = jwtHelper.verifyToken(request,isAjaxRequest);
	                if (decodedJWT != null) {
	                	if(decodedJWT.getExpiresAt().getTime()<System.currentTimeMillis()){
	                		//过期了
	                		if (isAjaxRequest) {
	                			long cha = System.currentTimeMillis()-decodedJWT.getExpiresAt().getTime();
		                		//如果大于7天
		                		if (cha>7*24*60*60*1000) {
		                			//超过7天没有登陆，则返回登陆
		                			String restr = "{\"code\":200,\"message\":\"new login\",\"data\":\"\"}";
									response.getWriter().print(restr);
								}else{
									String token = decodedJWT.getToken();
									//当第二个请求来的时候，我们可以去redis查询下是否存在这么个旧token，存在的话放行。
									//此处加同步锁是为了当第二个请求来的时候，确定第一个请求已经把旧的token放进redis了
									synchronized (this) {
										if (redisUtil.hasKey(token)) {
											chain.doFilter(request, response);
										}else{
											String userName = decodedJWT.getClaim("username").asString();
											//设置30秒的存活期
											redisUtil.set(token,userName,30);
											Map<String, Object> claims = new HashMap<String, Object>();
											claims.put("username", userName);
											token = jwtHelper.generateToken(claims);
											String restr = "{\"code\":201,\"message\":\"new token\",\"data\":\""+token+"\"}";
											response.getWriter().print(restr);
										}
									}
									
								}
							}else{
								response.sendRedirect("http://127.0.0.1:8090/corsWeb/index.html");
							}
	                		
	                	}else{
	                		chain.doFilter(request, response);
	                	}
	                	
	                    
	                }else{
	                	if (isAjaxRequest) {
	                		//是一个非法token
		                	String restr = "{\"code\":202,\"message\":\"no pass\",\"data\":\"\"}";
							response.getWriter().print(restr);
						}else{
							response.sendRedirect("http://127.0.0.1:8090/corsWeb/index.html");
						}
	                	
	                }
	            }
	        }
			
		}
	}

}
