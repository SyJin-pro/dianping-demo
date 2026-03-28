package com.jj.dianpingdemo.entity;

/**
 * @author: JSY
 * @version: 1.0
 */
public class LoginResult {
    private boolean success;
    private String message;

    public LoginResult() {
    }

    public LoginResult(boolean success, String message) {
        this.success = success;
        this.message = message;
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
}
