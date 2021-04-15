package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "State")
public class StateDemo {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> log.debug("{} running...", Thread.currentThread().getName()), "t1");
        Thread t2 = new Thread(() -> { while (true) {} }, "t2");
        Thread t3 = new Thread(() -> log.debug("{} running...", Thread.currentThread().getName()), "t3");
        Thread t4 = new Thread(() -> {
            synchronized (StateDemo.class) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t4");
        Thread t5 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t5");
        Thread t6 = new Thread(() -> {
            synchronized (StateDemo.class) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t4");
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("t1 => [{}]", t1.getState());   // TERMINATED
        log.debug("t2 => [{}]", t2.getState());   // RUNNABLE
        log.debug("t3 => [{}]", t3.getState());   // TERMINATED
        log.debug("t4 => [{}]", t4.getState());   // TIMED_WAITING
        log.debug("t5 => [{}]", t5.getState());   // WAITING
        log.debug("t6 => [{}]", t6.getState());   // BLOCKED
    }
}
