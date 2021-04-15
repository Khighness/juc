package top.parak.share;

import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-04-10
 */

@Slf4j(topic = "CorrectPostureStep2")
public class CorrectPostureStep2Demo {

    private static final Object room = new Object();
    private static boolean hasCigarette = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (room) {
                String name = Thread.currentThread().getName();
                log.debug("有烟吗 ? [{}]", hasCigarette);
                if (!hasCigarette) {
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
                }
            }
        }, "FlowerK").start();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.debug("可以开始干活了");
                }
            }, String.valueOf(i + 1)).start();
        }
        new Thread(() -> {
            synchronized (room) {
                hasCigarette = true;
                log.debug("烟到了");
                room.notify();
            }
        }, "饿了么").start();
    }
}
