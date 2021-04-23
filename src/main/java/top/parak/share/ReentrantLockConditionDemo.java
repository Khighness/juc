package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author KHighness
 * @since 2021-04-23
 */

@Slf4j(topic = "ReentrantLockCondition")
public class ReentrantLockConditionDemo {

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        // 创建两个新的条件变量
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        lock.lock();
        // 进入休息室等待
        condition1.await();
        // 唤醒condition1
        condition1.signal();
    }
}
