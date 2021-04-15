package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-06
 */

@Slf4j(topic = "JoinTime")
public class JoinTimeDemo {

    public static void main(String[] args) throws InterruptedException {
        time();
    }

    public static void time() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        long start = System.currentTimeMillis();
        log.debug("join begin");
        t1.join();
        log.debug("t1 join end");
        t2.join();
        log.debug("t2 join end");
        long end = System.currentTimeMillis();
        log.debug("time cost => [{}s]", (end - start) / 1000);
    }
}
