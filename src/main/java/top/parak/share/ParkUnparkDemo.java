package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author KHighness
 * @since 2021-04-13
 */

@Slf4j(topic = "ParkUnpark")
public class ParkUnparkDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("start...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("park...");
            LockSupport.park();
            log.debug("resume...");
        }, "t1");
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("unpark...");
        LockSupport.unpark(t1);
    }
}
