package top.parak.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @author KHighness
 * @since 2021-05-07
 */

@Slf4j(topic = "StampedLock")
public class StampedLockDemo {
    public static void main(String[] args) {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        new Thread(() -> {
            log.debug("read result{}", dataContainer.read(1));
        }, "t1").start();
        new Thread(() -> {
            dataContainer.write(3);
        }, "t2").start();
    }
}

@Slf4j(topic = "DataContainerStamped")
class DataContainerStamped {
    private int data;

    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data) {
        this.data = data;
    }

    public void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读
     * @param readTime 阅读时间
     * @return data
     */
    public int read(int readTime) {
        // 乐观锁读
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic read locking => [{}]", stamp);
        sleep(readTime, TimeUnit.SECONDS);
        // 验证戳记
        if (lock.validate(stamp)) {
            log.debug("read finish => [{}]", stamp);
            return data;
        }
        // 锁升级
        log.debug("updating to read lock => [{}]", stamp);
        try {
            stamp = lock.readLock();
            log.debug("read lock => [{}]", stamp);
            sleep(readTime, TimeUnit.SECONDS);
            log.debug("read finish => [{}]", stamp);
            return data;
        } finally {
            // 释放读锁
            log.debug("read unlock {}", stamp);
            lock.unlockRead(stamp);
        }
    }

    public void write(int newData) {
        // 获取写锁
        long stamp = lock.writeLock();
        log.debug("write lock => [{}]", stamp);
        try {
            sleep(1, TimeUnit.SECONDS);
            this.data = newData;
        } finally {
            // 释放读锁
            log.debug("write finish => [{}]", stamp);
            lock.unlockWrite(stamp);
        }
    }
}
