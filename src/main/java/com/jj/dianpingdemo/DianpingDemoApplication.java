package com.jj.dianpingdemo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class DianpingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DianpingDemoApplication.class, args);
    }

    // 启动后自动测试Redis
//    @Bean
//    public ApplicationRunner redisTest(StringRedisTemplate redisTemplate) {
//        return args -> {
//            // 存值
//            redisTemplate.opsForValue().set("username", "jj");
//            // 取值
//            String value = redisTemplate.opsForValue().get("username");
//            System.out.println("✅ Redis 测试成功！取出的值：" + value);
//        };
//    }
}
