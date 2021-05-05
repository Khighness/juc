package top.parak.aqs;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author KHighness
 * @since 2021-05-02
 * @apiNote 不可冲入独占锁
 */

public class ParaKLock implements Lock, Serializable {
    private static final long serialVersionUID = 7379874578553124018L;

    // 独占锁同步器
    private static class ParaKSync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 6278547169976524823L;

        @Override // 是否锁已经被持有
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        @Override // 如果state为0，则尝试获取锁
        protected boolean tryAcquire(int arg) {
            if (Thread.currentThread() == this.getExclusiveOwnerThread())
                throw new IllegalMonitorStateException("不支持锁重入");
            if (compareAndSetState(0, 1)) {
                // 设置独占者线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override // 尝释放锁，设置state = 0
        protected boolean tryRelease(int arg) {
            assert arg == 1;
            if (getState() == 0)
                throw new IllegalMonitorStateException("当前state不为0");
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        // 提供条件变量接口
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    private final ParaKSync sync = new ParaKSync();

    @Override // 一直尝试，失败进入AQS队列
    public void lock() {
        sync.acquire(1);
    }

    @Override // 尝试一次
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override // 超时尝试
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override // 解锁
    public void unlock() {
        sync.release(1);
    }

    // 是否被占有
    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    @Override // 可被打断式加锁
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
