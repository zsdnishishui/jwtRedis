package com.zsd.util;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtHelper {

    private Long EXPIRATION_TIME;
    private String SECRET;
    private final String TOKEN_PREFIX = "Bearer";
    private final String HEADER_STRING = "Authorization";
    private final String TOKEN_PARAMETER = "token";
    public JwtHelper(String secret, long expire) {
        this.EXPIRATION_TIME = expire;
        this.SECRET = secret;
        System.out.println("正在初始化Jwthelper，expire="+expire+":secret:"+SECRET);
    }

    public String generateToken(Map<String, Object> claims) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.SECOND, EXPIRATION_TIME.intValue());
        Date d = c.getTime();
        Algorithm al = Algorithm.HMAC256(SECRET);
        String token = JWT.create()
                .withSubject("userInfo")
                .withClaim("username", claims.get("username").toString())
                .withClaim("role", claims.get("role").toString())
                .withExpiresAt(d)
                .sign(al);
        return token;
    }

    public DecodedJWT verifyToken(HttpServletRequest request, boolean isAjaxRequest){
        String token ="";
        if (isAjaxRequest) {
        	token = request.getHeader(HEADER_STRING);
		}else{
			token = request.getParameter(TOKEN_PARAMETER);
		}
    	Algorithm algorithm = Algorithm.HMAC256(SECRET);
        
        
        DecodedJWT jwt=null;
        try {
        	jwt = JWT.decode(token);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
        //加密部分是不是相等
        byte[] signatureBytes = algorithm.sign(jwt.getHeader().getBytes(StandardCharsets.UTF_8), jwt.getPayload().getBytes(StandardCharsets.UTF_8));
        String signature = Base64.encodeBase64URLSafeString((signatureBytes));
        if (signature.equals(jwt.getSignature())) {
			return jwt;
		}else{
			return null;
		}
    }

}
