package top.parak.jmm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-25
 */

@Slf4j(topic = "InterruptRead")
public class InterruptReadDemo {
    private static int x;
    public static void main(String[] args) {
        Thread t2 = new Thread(() -> {
            while (true) {
                log.debug("x: {}", x);
                if (Thread.currentThread().isInterrupted()) {
                    log.debug("x: {}", x);
                    break;
                }
            }
        }, "t2");
        t2.start();
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x = 3;
            t2.interrupt();
        }, "t1").start();
        while (!t2.isInterrupted()) {
            Thread.yield();
        }
        log.debug("x: {}", x);
    }
}
