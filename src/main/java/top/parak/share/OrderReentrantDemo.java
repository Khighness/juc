package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author KHighness
 * @since 2021-04-23
 */

@Slf4j(topic = "OrderReentrant")
public class OrderReentrantDemo {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static boolean bRunned = false;
    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            lock.lock();
            try {
                if (!bRunned) {
                    condition.await();
                }
                log.debug("1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "A");
        Thread b = new Thread(() -> {
            lock.lock();
            try {
                log.debug("2");
                bRunned = true;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }, "B");
        a.start();
        b.start();
    }
}
