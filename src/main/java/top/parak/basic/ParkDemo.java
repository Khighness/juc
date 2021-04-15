package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author KHighness
 * @since 2021-04-06
 */

@Slf4j(topic = "Park")
public class ParkDemo {

    public static void main(String[] args) throws InterruptedException {
        park();
    }

    public static void park() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            log.debug("isInterrupted => [{}]", Thread.currentThread().isInterrupted());
            LockSupport.park();
            log.debug("unpark...");
            log.debug("isInterrupted => [{}]", Thread.currentThread().isInterrupted());
            LockSupport.park();
            Thread.interrupted();
            log.debug("isInterrupted => [{}]", Thread.currentThread().isInterrupted());
            log.debug("unpark...");
        }, "t1");
        t1.start();

        TimeUnit.SECONDS.sleep(1);
        t1.interrupt();
    }
}
