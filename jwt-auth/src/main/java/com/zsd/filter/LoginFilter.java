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
		// 200 重新登录，201 合法不过期，202 合法过期，203 不合法
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String uri = request.getRequestURI();
		//"OPTIONS" 是为了放过检测跨域的请求
		if (urls.contains(uri)||"OPTIONS".equals(request.getMethod())) {
			chain.doFilter(req, res); // 调用下一过滤器
		}else{


			boolean isAjaxRequest =false;
			if(request.getHeader("isAjax")!=null && request.getHeader("isAjax").equals("yes")){
				isAjaxRequest = true;
			}
			DecodedJWT decodedJWT = jwtHelper.verifyToken(request,isAjaxRequest);
			if (decodedJWT != null) {
				long cha = System.currentTimeMillis()-decodedJWT.getExpiresAt().getTime();
				if(cha > 0){
					//过期了
					if (isAjaxRequest) {
						//如果大于7天
						if (cha>7*24*60*60*1000) {
							//超过7天没有登陆，则返回登陆
							String restr = "{\"code\":200,\"message\":\"new login\",\"data\":\"\"}";
							response.getWriter().print(restr);
						}else{
							//202 合法过期
							String restr = "{\"code\":202,\"message\":\"expire\",\"data\":\"\"}";
							response.getWriter().print(restr);

						}
					}else{
						response.sendRedirect("http://127.0.0.1:8090/corsWeb/index.html");
					}

				}else{
//					String restr = "{\"code\":201,\"message\":\"pass\",\"data\":\"\"}";
//					response.getWriter().print(restr);
					chain.doFilter(req, res); // 调用下一过滤器
				}


			}else{
				//是一个非法token
				if (isAjaxRequest) {
					String restr = "{\"code\":203,\"message\":\"no pass\",\"data\":\"\"}";
					response.getWriter().print(restr);
				}else{
					response.sendRedirect("http://127.0.0.1:8090/corsWeb/index.html");
				}

			}
			
		}
	}

}
