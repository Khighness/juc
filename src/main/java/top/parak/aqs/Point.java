package top.parak.aqs;

import java.util.concurrent.locks.StampedLock;

/**
 * @author KHighness
 * @since 2021-05-05
 */

public class Point {
    /**
     * 横坐标
     */
    private double x;
    /**
     * 纵坐标
     */
    private double y;
    /**
     * 锁实例
     */
    private final StampedLock lock = new StampedLock();

    /**
     * 移动点到新位置
     * <p>使用排它锁-写锁</p>
     * @param deltaX 横坐标变化值
     * @param deltaY 纵坐标变化值
     */
    void move(double deltaX, int deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    /**
     * 计算当前位置到原点的距离
     * <p>使用乐观锁读</p>
     * @return 距离原点的距离
     */
    double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();
        double currentX = x, currentY = y;
        // 检查获取stamp后，锁是否被其他写线程排他性抢占
        if (!lock.validate(stamp)) {
            // 如果被抢占则进行锁升级，获取悲观读锁
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    /**
     * 如果当前位置在原点则移动至新位置
     * <p>使用悲观锁获取读锁，并尝试转换为写锁</p>
     * @param newX 新位置的横坐标
     * @param newY 新位置的纵坐标
     */
    void moveIfAtOrigin(double newX, double newY) {
        // 这里可以用乐观锁读替换
        long stamp = lock.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                // 尝试将读锁升级为写锁
                long ws = lock.tryConvertToWriteLock(stamp);
                // 升级成功，则更换戳记，更新坐标，退出循环
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                }
                // 升级失败，则释放读锁，显式获取写锁，循环重试
                else {
                    lock.unlockRead(stamp);
                    stamp = lock.writeLock();
                }
            }
        } finally {
            // 释放锁
            lock.unlockWrite(stamp);
        }
    }
}
