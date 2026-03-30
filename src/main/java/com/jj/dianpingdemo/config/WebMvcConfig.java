package com.jj.dianpingdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.jj.dianpingdemo.util.SessionConstants.*;

/**
 * @author: JSY
 * @version: 1.0
 */
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        USER_CODE_PATH,
//                        USER_LOGIN_PATH,
//                        USER_LOGOUT_PATH,
//                        //USER_ID_PATH,
//                        "/error",
//                        "/favicon.ico"
//                );
//    }
//}

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final StringRedisTemplate stringRedisTemplate;

    public WebMvcConfig(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate))
                .addPathPatterns("/**")
                .order(0);

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        USER_CODE_PATH,
                        USER_LOGIN_PATH,
                        USER_LOGOUT_PATH,
                        "/error",
                        "/favicon.ico"
                )
                .order(1);
    }
}