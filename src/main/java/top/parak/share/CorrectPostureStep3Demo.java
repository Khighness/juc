package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-10
 */

@Slf4j(topic = "CorrectPostureStep3")
public class CorrectPostureStep3Demo {

    private static final Object room = new Object();
    private static boolean hasCigarette = false;
    private static boolean hasTakeAway = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (room) {
                String name = Thread.currentThread().getName();
                log.debug("有烟吗 ? [{}]", hasCigarette);
                while (!hasCigarette) { // 防止虚假唤醒
                    log.debug("没有烟 => [{}]先歇会...", name);
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("再看看有烟吗 ? [{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("有烟了 => [{}]开始干活ing", name);
                } else {
                    log.debug("依然没有烟 => [{}]不干活了", name);
                }
            }
        }, "FlowerK").start();
        new Thread(() -> {
            synchronized (room) {
                String name = Thread.currentThread().getName();
                log.debug("外卖是否送到 ? [{}]", hasTakeAway);
                if (!hasTakeAway) {
                    log.debug("外卖还未送到 => [{}]先歇会...", name);
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("再看看外卖是否送到 ? [{}]", hasTakeAway);
                if (hasTakeAway) {
                    log.debug("外卖已经送到 => [{}]开始干活ing", name);
                } else {
                    log.debug("外卖仍然未到 => [{}]不干活了", hasTakeAway);
                }
            }
        }, "RubbishK").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            synchronized (room) {
                hasTakeAway = true;
                log.debug("外卖已送到");
                room.notifyAll();
            }
        }, "美团外卖").start();
    }

}
