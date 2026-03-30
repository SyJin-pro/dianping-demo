package com.jj.dianpingdemo.entity;

/**
 * @author: JSY
 * @version: 1.0
 */
public class Result<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> Result<T> ok(String message, T data) {
        Result<T> r = new Result<>();
        r.success = true;
        r.message = message;
        r.data = data;
        return r;
    }

    public static <T> Result<T> ok(String message) {
        return ok(message, null);
    }

    // 新增：仅返回数据
    public static <T> Result<T> ok(T data) {
        return ok("success", data);
    }
    // 新增：无数据无自定义消息
    public static <T> Result<T> ok() {
        return ok("success", null);
    }
    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.success = false;
        r.message = message;
        r.data = null;
        return r;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
