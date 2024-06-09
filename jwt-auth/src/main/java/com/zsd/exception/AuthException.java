package com.zsd.exception;

/**
 * 权限的自定义注解
 */
public class AuthException extends RuntimeException {
    /**
     * 异常构造方法 在使用时方便传入错误码和信息
     */
    public AuthException(String msg) {
        super(msg);
    }
}
