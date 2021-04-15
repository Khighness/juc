package top.parak.lock8;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "LockTest3")
public class LockDemo3 {
    @SneakyThrows
    public static void main(String[] args) {
        Lock3 lock = new Lock3();
        new Thread(lock::printA, "A").start();
        new Thread(lock::printB, "B").start();
        new Thread(lock::printC, "C").start();
    }
}

@Slf4j(topic = "Lock3")
class Lock3 {
    @SneakyThrows
    public synchronized void printA() {
        TimeUnit.SECONDS.sleep(1); // 会让出CPU执行权
        log.debug("A => [{}]", System.nanoTime());
    }
    public synchronized void printB() {
        log.debug("B => [{}]", System.nanoTime());
    }
    public void printC() {
        log.debug("C => [{}]", System.nanoTime());
    }
}
