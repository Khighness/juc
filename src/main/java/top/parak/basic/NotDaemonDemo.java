package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "NotDaemon")
public class NotDaemonDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            log.debug("{} end", Thread.currentThread().getName());
        }, "t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("{} end", Thread.currentThread().getName());
    }
}
