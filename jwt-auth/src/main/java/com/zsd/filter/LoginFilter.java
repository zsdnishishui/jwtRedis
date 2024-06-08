package com.zsd.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zsd.util.JwtHelper;
import com.zsd.util.RedisUtil;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
		String uri = request.getRequestURI();
		//"OPTIONS" 是为了放过检测跨域的请求
		if (urls.contains(uri)) {
			chain.doFilter(req, res); // 调用下一过滤器
		}else{
			DecodedJWT decodedJWT = jwtHelper.verifyToken(request);
			if (decodedJWT != null && System.currentTimeMillis() <= decodedJWT.getExpiresAt().getTime()) {
				chain.doFilter(req, res); // 调用下一过滤器
			}else{
				//是一个非法token或过期了
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().print("{\"code\":401,\"message\":\"token不合法或已过期\",\"data\":\"\"}");
			}
			
		}
	}

}
