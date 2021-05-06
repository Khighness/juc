package top.parak.aqs;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author KHighness
 * @since 2021-05-05
 */

public class CachedDataDemo {
}

class CachedData {
    Object data;
    // 是否有效，如果失效，需要重新计算data
    volatile boolean cacheValid;
    final static Random RANDOM = new Random();
    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    void processCachedData() {
        lock.readLock().lock();
        if (!cacheValid) {
            // 不支持锁升级，获取写锁钱必须释放读锁
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                // 双重检查，判断是否其他线程已经获取了读锁更新缓存，避免重复更新
                if (!cacheValid) {
                    // data = write();
                    cacheValid = true;
                }
                // 降为读锁，释放写锁，让其他线程读取缓存
                lock.readLock().lock();
            } finally {
                lock.writeLock().unlock();
            }
        }
        // 使用数据，释放读锁
        try {
            // use(data);
        } finally {
            lock.readLock().unlock();
        }
    }
}
