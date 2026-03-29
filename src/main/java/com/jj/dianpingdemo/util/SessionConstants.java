package com.jj.dianpingdemo.util;

/**
 * @author: JSY
 * @version: 1.0
 */


public final class SessionConstants {

    private SessionConstants() {
    }

    /** Session 中登录用户的统一键名 */
    public static final String LOGIN_USER_KEY = "login:user";

    /** 验证码（mock/后续redis验证码都可复用此key前缀） */
    public static final String LOGIN_CODE_KEY_PREFIX = "login:code:";

    /** 登录接口白名单路径 */
    public static final String USER_CODE_PATH = "/user/code";
    public static final String USER_LOGIN_PATH = "/user/login";
    public static final String USER_LOGOUT_PATH = "/user/logout";
    public static final String USER_ID_PATH = "/user/*";
}