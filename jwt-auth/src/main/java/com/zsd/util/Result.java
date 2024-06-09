package com.zsd.util;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return new Result(0, "操作成功", null);
    }

    public static Result success(String message) {
        return new Result(0, message, null);
    }

    /**
     * 操作成功
     *
     * @param message 提示语
     * @param data    返回的数据
     * @return
     */
    public static Result success(String message, Object data) {
        return new Result(0, message, data);
    }

    public static Result fail() {
        return new Result(-1, "操作失败", null);
    }

    /**
     * 操作失败
     *
     * @param message 提示语
     * @return
     */
    public static Result fail(String message) {
        return new Result(-1, message, null);
    }

    public static Result fail(String message, Object data) {
        return new Result(-1, message, data);
    }
}
