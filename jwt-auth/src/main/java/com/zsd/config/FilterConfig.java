package com.zsd.config;

import com.zsd.filter.LoginFilter;
import com.zsd.util.JwtHelper;
import com.zsd.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FilterConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private long expire;

    @Value("${jwt.except.authorised-urls}")
    private String[] authorisedUrls;

    @Bean
    public JwtHelper jwtHelperBean() {
        return new JwtHelper(secret, expire);
    }
    @Bean
    public RedisUtil redisUtilBean() {
        return new RedisUtil();
    }
    @Bean
    public FilterRegistrationBean basicFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LoginFilter filter = new LoginFilter(jwtHelperBean(), authorisedUrls,redisUtilBean());
        registrationBean.setFilter(filter);
        registrationBean.setName("loginFilter");
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setOrder(Integer.MAX_VALUE);
        return registrationBean;
    }
    
}