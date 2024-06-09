package com.zsd.annotation;

import com.alibaba.fastjson.JSON;
import com.zsd.entity.User;
import com.zsd.util.JwtHelper;
import com.zsd.util.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class PreAuthAspect {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtHelper jwtHelper;

    @Pointcut("@annotation(PreAuth)")
    public void preAuthPointCut() {}


    @Before("preAuthPointCut()")
    public void  beforeAuth(JoinPoint joinPoint) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取操作
        PreAuth preAuth = method.getAnnotation(PreAuth.class);

        Object userObject = redisUtil.get("username:"+jwtHelper.getUsername(request));
        if (userObject != null){
            User user = JSON.parseObject(userObject.toString(),User.class);
            List<String> authList = user.getAuthList();
            String auth = preAuth.value();
            if (!authList.contains(auth)) {
                throw new Exception("无此权限");
            }
        } else{
            throw new Exception("没有此用户的信息");
        }

    }
}
