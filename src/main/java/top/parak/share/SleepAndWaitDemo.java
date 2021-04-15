package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-10
 */

@Slf4j(topic = "SleepAndWait")
public class SleepAndWaitDemo {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock) {
                log.debug("获得锁");
                try {
//                    Thread.sleep(20_000);  // (1)
                    lock.wait(20_000);   // (2)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();
        TimeUnit.SECONDS.sleep(1);
        synchronized (lock) {
            log.debug("获得锁");
        }
    }
}
