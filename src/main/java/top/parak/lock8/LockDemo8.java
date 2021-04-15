package top.parak.lock8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "LockTest8")
public class LockDemo8 {
    public static void main(String[] args) {
        new Thread(Lock8::printA, "A").start();
        new Thread(Lock8::printB, "B").start();
    }
}

@Slf4j(topic = "Lock8")
class Lock8 {
    @SneakyThrows
    public static synchronized void printA() {
        TimeUnit.SECONDS.sleep(1);
        log.debug("A => [{}]", System.nanoTime());
    }
    public static synchronized void printB() {
        log.debug("B => [{}]", System.nanoTime());
    }
}
