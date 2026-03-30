package com.jj.dianpingdemo.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author: JSY
 * @version: 1.0
 */
@Component
public class RedisIdWorker {

    // 自定义开始时间: 2022-01-01 00:00:00 UTC
    private static final long BEGIN_TIMESTAMP = 1640995200L;
    // 低位序列号位数
    private static final int SEQ_BITS = 32;

    private final StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long nextId(String keyPrefix) {
        // 1) 生成 31bit 时间偏移（秒）
        long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long timestampOffset = nowSecond - BEGIN_TIMESTAMP;

        // 2) 生成 32bit 秒内序列
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long seq = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        // 3) 左移 + 或运算拼接
        return (timestampOffset << SEQ_BITS) | (seq == null ? 0 : seq);
    }
}
