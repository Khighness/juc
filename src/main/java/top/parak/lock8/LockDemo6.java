package top.parak.lock8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "LockTest6")
public class LockDemo6 {
    public static void main(String[] args) {
        new Thread(Lock6::printA, "A").start();
        new Thread(Lock6::printB, "B").start();
    }
}

@Slf4j(topic = "Lock6")
class Lock6 {
    @SneakyThrows
    public static synchronized void printA() {
        TimeUnit.SECONDS.sleep(1);
        log.debug("A => [{}]", System.nanoTime());
    }
    public static synchronized void printB() {
        log.debug("B => [{}]", System.nanoTime());
    }
}

