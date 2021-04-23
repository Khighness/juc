package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @author KHighness
 * @since 2021-04-23
 */

@Slf4j(topic = "OrderParkUnpark")
public class OrderParkUnparkDemo {
    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            LockSupport.park();
            log.debug("1");
        }, "A");
        Thread b = new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(a);
        }, "B");
        a.start();
        b.start();
    }
}
