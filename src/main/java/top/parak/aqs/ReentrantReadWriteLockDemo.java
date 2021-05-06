package top.parak.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author KHighness
 * @since 2021-05-05
 */

@Slf4j(topic = "1")
public class ReentrantReadWriteLockDemo {
    public static void main(String[] args) throws InterruptedException {
        DataContainer container = new DataContainer();
        new Thread(container::write, "w1").start();
        new Thread(container::write, "w2").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(container::read, "r1").start();
        new Thread(container::read, "r2").start();
    }
}

@Slf4j(topic = "DataContainer")
class DataContainer {
    private Object data;
    private final static Random RANDOM = new Random();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private static Character randomChar() {
        int choice = RANDOM.nextInt(3);
        if (choice == 0)
            return (char) (RANDOM.nextInt(10) + 48);
        else if (choice == 1)
            return (char) (RANDOM.nextInt(26) + 65);
        else
            return (char) (RANDOM.nextInt(26) + 97);
    }

    public void read() {
        log.debug("获取读锁...");
        readLock.lock();
        try {
            log.debug("读取数据 => [{}]", data);
        } finally {
            log.debug("释放读锁...");
            readLock.unlock();
        }
    }

    public void write() {
        log.debug("获取写锁...");
        writeLock.lock();
        try {
            log.debug("写入数据 => [{}]", data = randomChar());
        } finally {
            log.debug("释放写锁...");
            writeLock.unlock();
        }
    }
}
