package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author KHighness
 * @since 2021-04-23
 */

@Slf4j(topic = "TryLock")
public class TryLockDemo {
    private static final ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("尝试获取锁");
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)) { // 尝试等待2S，获取锁
                    log.debug("获取不到锁");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("获取不到锁");
                return;
            }
            try {
                log.debug("获取到锁");
            } finally {
                lock.unlock();
            }
        }, "t1");
        lock.lock();
        log.debug("获取到锁");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        lock.unlock();
        log.debug("释放了锁");
    }
}
