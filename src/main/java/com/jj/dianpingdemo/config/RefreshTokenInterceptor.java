package com.jj.dianpingdemo.config;

import com.jj.dianpingdemo.entity.UserDto;
import com.jj.dianpingdemo.util.SessionConstants;
import com.jj.dianpingdemo.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: JSY
 * @version: 1.0
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(SessionConstants.AUTHORIZATION_HEADER);
        if (token == null || token.isBlank()) {
            return true; // 无 token 直接放行，交给登录拦截器决定是否拦截
        }

        String tokenKey = SessionConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(tokenKey);
        if (userMap == null || userMap.isEmpty()) {
            return true;
        }

        UserDto userDto = new UserDto();
        Object id = userMap.get("id");
        Object phone = userMap.get("phone");
        Object nickName = userMap.get("nickName");
        if (id != null) {
            userDto.setId(Long.valueOf(String.valueOf(id)));
        }
        userDto.setPhone(phone == null ? null : String.valueOf(phone));
        userDto.setNickName(nickName == null ? null : String.valueOf(nickName));

        UserHolder.saveUser(userDto);

        // 刷新 token TTL
        stringRedisTemplate.expire(tokenKey, SessionConstants.LOGIN_USER_TTL);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserHolder.removeUser();
    }
}