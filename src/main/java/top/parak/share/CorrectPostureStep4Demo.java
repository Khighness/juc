package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author KHighness
 * @since 2021-04-23
 */

@Slf4j(topic = "CorrectPostureStep4")
public class CorrectPostureStep4Demo {
    static boolean hasCigarette = false;
    static boolean hasTakeAway = false;
    static ReentrantLock ROOM = new ReentrantLock();
    // 等烟的休息室
    static Condition waitCigaretteSet = ROOM.newCondition();
    // 等外卖的休息室
    static Condition waitTakeAwaySet = ROOM.newCondition();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            ROOM.lock();
            try {
                String name = Thread.currentThread().getName();
                log.debug("有烟吗 ? [{}]", hasCigarette);
                while (!hasCigarette) { // 防止虚假唤醒
                    log.debug("没有烟 => [{}]先歇会...", name);
                    waitCigaretteSet.await();
                }
                log.debug("再看看有烟吗 ? [{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("有烟了 => [{}]开始干活ing", name);
                } else {
                    log.debug("依然没有烟 => [{}]不干活了", name);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                ROOM.unlock();
            }
        }, "FlowerK").start();
        new Thread(() -> {
            ROOM.lock();
            try {
                String name = Thread.currentThread().getName();
                log.debug("外卖是否送到 ? [{}]", hasTakeAway);
                if (!hasTakeAway) {
                    log.debug("外卖还未送到 => [{}]先歇会...", name);
                    waitTakeAwaySet.await();
                }
                log.debug("再看看外卖是否送到 ? [{}]", hasTakeAway);
                if (hasTakeAway) {
                    log.debug("外卖已经送到 => [{}]开始干活ing", name);
                } else {
                    log.debug("外卖仍然未到 => [{}]不干活了", hasTakeAway);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                ROOM.unlock();
            }
        }, "RubbishK").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            ROOM.lock();
            try {
                hasTakeAway = true;
                waitTakeAwaySet.signal();
                log.debug("外卖已送到");
            } finally {
                ROOM.unlock();
            }
        }, "美团外卖").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            ROOM.lock();
            try {
                hasCigarette = true;
                waitCigaretteSet.signal();
                log.debug("烟已送到");
            } finally {
                ROOM.unlock();
            }
        }, "饿了么").start();
    }
}
