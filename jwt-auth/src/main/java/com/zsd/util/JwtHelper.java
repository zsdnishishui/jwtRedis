package com.zsd.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
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
    public final static String TOKEN_PREFIX = "Bearer ";
    public final static String HEADER_STRING = "Authorization";

    public JwtHelper(String secret, long expire) {
        this.EXPIRATION_TIME = expire;
        this.SECRET = secret;
        System.out.println("正在初始化Jwthelper，expire=" + expire + ":secret:" + SECRET);
    }

    /**
     * 生成token
     *
     * @param claims
     * @return
     */
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

    /**
     * 校验token
     *
     * @param request
     * @return
     */
    public DecodedJWT verifyToken(HttpServletRequest request) {

        DecodedJWT jwt = getDecodeJwt(request);
        if (jwt != null) {
            //加密部分是不是相等
            byte[] signatureBytes = Algorithm.HMAC256(SECRET).sign(jwt.getHeader().getBytes(StandardCharsets.UTF_8), jwt.getPayload().getBytes(StandardCharsets.UTF_8));
            String signature = Base64.encodeBase64URLSafeString((signatureBytes));
            if (signature.equals(jwt.getSignature())) {
                return jwt;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取token中的username
     *
     * @param request
     * @return
     */
    public String getUsername(HttpServletRequest request) {
        DecodedJWT jwt = getDecodeJwt(request);
        if (jwt != null) {
            Claim getClaim = jwt.getClaim("username");
            if (getClaim != null) {
                return getClaim.asString();
            }
        }
        return null;
    }

    /**
     * 获取解码后的jwt
     *
     * @param request
     * @return
     */
    private DecodedJWT getDecodeJwt(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            DecodedJWT jwt = null;
            try {
                jwt = JWT.decode(token.substring(7));
                return jwt;
            } catch (Exception e) {
                // TODO: handle exception
                return null;
            }
        } else {
            return null;
        }

    }
}
