package top.parak.lock8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "LockTest1")
public class LockDemo1 {
    public static void main(String[] args) throws InterruptedException {
        Lock1 lock = new Lock1();
        new Thread(lock::printA, "A").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(lock::printB, "B").start();
    }
}
@Slf4j(topic = "Lock1")
class Lock1 {
    public synchronized void printA() { log.debug("A => [{}]", System.nanoTime()); }
    public synchronized void printB() { log.debug("B => [{}]", System.nanoTime()); }
}
