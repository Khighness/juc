package top.parak.lock8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "LockTest2")
public class LockDemo2 {
    @SneakyThrows
    public static void main(String[] args) {
        Lock2 lock = new Lock2();
        new Thread(lock::printA, "A").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(lock::printB, "B").start();
    }
}

@Slf4j(topic = "Lock2")
class Lock2 {
    @SneakyThrows
    public synchronized void printA() {
        TimeUnit.SECONDS.sleep(1);
        log.debug("A => [{}]", System.nanoTime());
    }
    public synchronized void printB() {
        log.debug("B => [{}]", System.nanoTime());
    }
}
