package top.parak.lock8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "LockTest7")
public class LockDemo7 {
    public static void main(String[] args) {
        Lock7 lock7 = new Lock7();
        new Thread(Lock7::printA, "A").start();
        new Thread(lock7::printB, "B").start();
    }
}

@Slf4j(topic = "Lock7")
class Lock7 {
    @SneakyThrows
    public static synchronized void printA() {
        TimeUnit.SECONDS.sleep(1);
        log.debug("A => [{}]", System.nanoTime());
    }
    public synchronized void printB() {
        log.debug("B => [{}]", System.nanoTime());
    }
}
