package top.parak.aqs;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author KHighness
 * @since 2021-05-03
 */

@Slf4j(topic = "ParkInterrupt")
public class ParkInterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            log.debug("1");
            LockSupport.park();
            log.debug("2");
        }, "t");
        t.start();
        TimeUnit.SECONDS.sleep(1);
        t.interrupt();
    }
}
