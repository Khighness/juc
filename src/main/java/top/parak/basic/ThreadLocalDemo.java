package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-08
 */

@Slf4j(topic = "ThreadLocal")
public class ThreadLocalDemo {

    private static ThreadLocal<String> local = new ThreadLocal<String>();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            local.set("KHighness");
            log.debug("init => [{}]", local.get());
        }, "init").start();
        Thread.sleep(100);
        new Thread(() -> {
            log.debug("before {} update => [{}]", Thread.currentThread().getName(), local.get());
            local.set("RubbishK");
            log.debug("after {} update => [{}]", Thread.currentThread().getName(), local.get());
        }, "t1").start();
        new Thread(() -> {
            log.debug("before {} update => [{}]", Thread.currentThread().getName(), local.get());
            local.set("FlowerK");
            log.debug("after {} update => [{}]", Thread.currentThread().getName(), local.get());
        }, "t2").start();
    }
}
