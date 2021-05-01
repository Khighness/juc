package top.parak.common;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-05-01
 */

@Slf4j(topic = "SynchronousQueue")
public class SynchronousQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> integers = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                log.debug("putting {}", 1);
                integers.put(1);
                log.debug("{} finish", 1);

                log.debug("putting {}", 2);
                integers.put(2);
                log.debug("{} finish", 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
           try {
               log.debug("taking {}", 1);
               integers.take();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }, "t2").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            try {
                log.debug("taking {}", 2);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t3").start();
    }
}
