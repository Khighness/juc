package top.parak.lock8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "LockTest5")
public class LockDemo5 {
    public static void main(String[] args) {
        Lock5 lock5 = new Lock5();
        new Thread(Lock5::printA, "A").start();
        new Thread(lock5::printB, "B").start();
    }
}

@Slf4j(topic = "Lock5")
class Lock5 {
    @SneakyThrows
    public static synchronized void printA() {
        TimeUnit.SECONDS.sleep(1);
        log.debug("A => [{}]", System.nanoTime());
    }
    public synchronized void printB() {
        log.debug("B => [{}]", System.nanoTime());
    }
}
