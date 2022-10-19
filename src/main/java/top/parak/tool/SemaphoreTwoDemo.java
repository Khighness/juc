package top.parak.tool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * @author KHighness
 * @since 2022-10-19
 * @apiNote 两个线程交替打印1-100
 */
@Slf4j(topic = "SemaphoreTwoDemo")
public class SemaphoreTwoDemo {
    private static volatile int i = 0;
    private static Semaphore s0 = new Semaphore(1);
    private static Semaphore s1 = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        s1.acquire();
        new Thread(() -> {
            while ((i + 1) < 100) {
                try {
                    s0.acquire();
                    log.info("=> {}", ++i);
                } catch (Exception e) {
                    log.error(e.getMessage());
                } finally {
                    s1.release();
                }
            }
        }, "a").start();
        new Thread(() -> {
            while ((i + 1) < 100) {
                try {
                    s1.acquire();
                    log.info("=> {}", ++i);
                } catch (Exception e) {
                    log.error(e.getMessage());
                } finally {
                    s0.release();
                }
            }
        }, "b").start();
    }
}
