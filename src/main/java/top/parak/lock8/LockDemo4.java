package top.parak.lock8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "LockTest4")
public class LockDemo4 {
    public static void main(String[] args) {
        Lock4 lock1 = new Lock4();
        Lock4 lock2 = new Lock4();
        new Thread(lock1::printA, "A").start();
        new Thread(lock2::printB, "B").start();
    }
}

@Slf4j(topic = "Lock4")
class Lock4 {
    @SneakyThrows
    public synchronized void printA() {
        TimeUnit.SECONDS.sleep(1);
        log.debug("A => [{}]", System.nanoTime());
    }
    public synchronized void printB() {
        log.debug("B => [{}]", System.nanoTime());
    }
}
