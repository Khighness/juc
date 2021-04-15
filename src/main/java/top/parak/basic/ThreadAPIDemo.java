package top.parak.basic;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author KHighness
 * @since 2021-04-06
 */

@Slf4j(topic = "ThreadAPI")
public class ThreadAPIDemo {

    @Test
    public void test1() {
        Thread t1 = new Thread(() -> log.debug("running"), "t1");
        t1.start();
        log.debug("do other things");
    }

    @Test
    public void test2() {
        Thread t1 = new Thread(() -> log.debug("running"), "t1");
        log.debug("t1 => [{}]", t1.getState());
        t1.start();
        log.debug("t1 => [{}]", t1.getState());
    }

    @SneakyThrows
    @Test
    public void test3() {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        log.debug("t1 => [{}]", t1.getState());
        Thread.sleep(500);
        log.debug("t1 => [{}]", t1.getState());
    }

    @SneakyThrows
    @Test
    public void test4() {
        Thread t1 = new Thread(() -> {
            log.debug("enter sleeping");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("wake up");
        }, "t1");
        t1.start();
        Thread.sleep(1000);
        log.debug("interrupt t1");
        t1.interrupt();
    }

    @Test
    public void test5() {
        Runnable task1 = () -> {
            int count = 0;
            for (;;) {
//                Thread.yield();
                System.out.printf("1 => [%d]\n", count++);
            }
        };
        Runnable task2 = () -> {
            int count = 0;
            for (;;) {
                System.out.printf("2 => [%d]\n", count++);
            }
        };
        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
    }

}
