package top.parak.tool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author KHighness
 * @since 2021-05-07
 */

@Slf4j(topic = "Semaphore")
public class SemaphoreDemo {
    private static final Semaphore semaphore = new Semaphore(0);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        executorService.submit(() -> {
            try {
                log.debug("over");
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.submit(() -> {
            try {
                log.debug("over");
                semaphore.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 等待子线程执行完毕
        semaphore.acquire(2);
        log.debug("all child thread over");
        executorService.shutdown();
    }
}
