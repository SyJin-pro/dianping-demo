package com.jj.dianpingdemo;

import com.jj.dianpingdemo.util.RedisIdWorker;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class DianpingDemoApplicationTests {

    @Resource
    private RedisIdWorker redisIdWorker;

    // 固定线程池，避免频繁创建线程带来的开销
    private static final ExecutorService es = Executors.newFixedThreadPool(500);

    @Test
    void testIdWorker() throws InterruptedException {
        int threadCount = 300;
        int idsPerThread = 100;
        int expectedTotal = threadCount * idsPerThread;

        CountDownLatch latch = new CountDownLatch(threadCount);
        // 线程安全Set，用于去重校验
        Set<Long> idSet = ConcurrentHashMap.newKeySet(expectedTotal);

        Runnable task = () -> {
            for (int i = 0; i < idsPerThread; i++) {
                long id = redisIdWorker.nextId("order");
                idSet.add(id);
            }
            latch.countDown();
        };

        long begin = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            es.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();

        int uniqueCount = idSet.size();
        int duplicateCount = expectedTotal - uniqueCount;
        long costMs = end - begin;
        double tps = costMs == 0 ? expectedTotal : (expectedTotal * 1000.0 / costMs);

        System.out.println("expectedTotal = " + expectedTotal);
        System.out.println("uniqueCount   = " + uniqueCount);
        System.out.println("duplicateCount= " + duplicateCount);
        System.out.println("time(ms)      = " + costMs);
        System.out.printf("tps(ids/s)    = %.2f%n", tps);
    }

}
