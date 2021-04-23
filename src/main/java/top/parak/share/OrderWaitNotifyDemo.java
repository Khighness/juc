package top.parak.share;

import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-04-23
 */

@Slf4j(topic = "OrderWaitNotify")
public class OrderWaitNotifyDemo {
    private static final Object lock = new Object();
    private static boolean bRunned = false; // 表示t2是否运行
    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            synchronized (lock) {
                while (!bRunned) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("1");
        }, "A");
        Thread b = new Thread(() -> {
            synchronized (lock)  {
                log.debug("2");
                bRunned = true;
                lock.notify();
            }
        }, "B");
        a.start();
        b.start();
    }
}
