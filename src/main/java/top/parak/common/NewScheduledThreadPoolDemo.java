package top.parak.common;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KHighness
 * @since 2021-05-01
 */

@Slf4j(topic = "NewScheduledThreadPool")
public class NewScheduledThreadPoolDemo {
    private static final ScheduledExecutorService pool = Executors.newScheduledThreadPool(2,
            new ThreadFactory() {
                private final AtomicInteger count = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "k-pool-thread-" + count.getAndIncrement());
                }
            });

    public static void main(String[] args) throws InterruptedException {
        method3();
    }

    public static void method1() throws InterruptedException {
        pool.schedule(() -> {
            log.debug("task1");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int i = 1 / 0;
        }, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(1);
        pool.schedule(() -> {
            log.debug("task2");
        }, 1, TimeUnit.SECONDS);
    }

    public static void method2() {
        log.debug("start...");
        // 延迟一秒执行，间隔时间为Max(设置时间, 线程睡眠时间)
        pool.scheduleAtFixedRate(() -> {
            log.debug("running");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                int i = 1 / 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public static void method3() {
        log.debug("start...");
        // 延迟一秒执行，间隔时间为设置时间+线程睡眠时间
        pool.scheduleWithFixedDelay(() -> {
            log.debug("running");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                int i = 1 / 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
