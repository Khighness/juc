package top.parak.jmm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-23
 */

@Slf4j(topic = "While")
public class WhileDemo {
    private static volatile boolean run = true;
    private static final Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            while (run) {
                synchronized (lock) { }
                System.out.println();
            }
        }, "t");
        t.start();
        TimeUnit.SECONDS.sleep(1);
        run = false;
        synchronized (lock) {
            run = false;
        }
    }
}
