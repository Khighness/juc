package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author KHighness
 * @since 2021-04-23
 */

public class AlternateAwaitSignalDemo {
    public static void main(String[] args) throws InterruptedException {
        AlternateAwaitSignal alternateWaitNotify = new AlternateAwaitSignal(5);
        Condition aCondition = alternateWaitNotify.newCondition();
        Condition bCondition = alternateWaitNotify.newCondition();
        Condition cCondition = alternateWaitNotify.newCondition();
        new Thread(() -> alternateWaitNotify.print("a", aCondition, bCondition), "A").start();
        new Thread(() -> alternateWaitNotify.print("b", bCondition, cCondition), "B").start();
        new Thread(() -> alternateWaitNotify.print("c", cCondition, aCondition), "C").start();
        alternateWaitNotify.lock();
        try {
            aCondition.signal();
        } finally {
            alternateWaitNotify.unlock();
        }
    }
}

@Slf4j(topic = "AlternateAwaitSignal")
class AlternateAwaitSignal extends ReentrantLock {
    private final int loopNumber;

    public AlternateAwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    /**
     * @param str   打印内容
     * @param curr  当前休息室
     * @param next  下个休息室
     */
    public void print(String str, Condition curr, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                curr.await();
                log.debug(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }
}
