package com.jj.dianpingdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jj.dianpingdemo.entity.Result;
import com.jj.dianpingdemo.entity.User;
import com.jj.dianpingdemo.util.SessionConstants;
import com.jj.dianpingdemo.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

/**
 * @author: JSY
 * @version: 1.0
 */
//public class LoginInterceptor implements HandlerInterceptor {
//
//    //private static final String USER_SESSION_KEY = "loginUser";
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession(false);
//        System.out.println("[preHandle] uri=" + request.getRequestURI()
//                + ", session=" + (session == null ? "null" : session.getId()));
//        if (session == null) {
//            writeUnauthorized(response);
//            return false;
//        }
//        Object obj = session.getAttribute(SessionConstants.LOGIN_USER_KEY);
//        System.out.println("[preHandle] key=" + SessionConstants.LOGIN_USER_KEY + ", val=" + obj);
//        if (!(obj instanceof User user)) {
//            writeUnauthorized(response);
//            return false;
//        }
//        UserHolder.saveUser(user);
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        UserHolder.removeUser();
//    }
//
//    private void writeUnauthorized(HttpServletResponse response) throws Exception {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.getWriter().write(objectMapper.writeValueAsString(Result.fail("未登录")));
//    }
//}

public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UserHolder.getUser() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail("未登录")));
            return false;
        }
        return true;
    }
}
