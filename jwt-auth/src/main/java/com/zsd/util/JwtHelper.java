package com.zsd.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtHelper {

    private Long EXPIRATION_TIME;
    private String SECRET;
    private final String TOKEN_PREFIX = "Bearer ";
    private final String HEADER_STRING = "Authorization";
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
                .withExpiresAt(d)
                .sign(al);
        return token;
    }

    public DecodedJWT verifyToken(HttpServletRequest request){
        String token  = request.getHeader(HEADER_STRING);
        if (token !=null && token.startsWith(TOKEN_PREFIX)) {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            DecodedJWT jwt=null;
            try {
                jwt = JWT.decode(token.substring(7));
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
        } else {
            return null;
        }

    }
}
