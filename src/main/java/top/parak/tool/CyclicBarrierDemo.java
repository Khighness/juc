package top.parak.tool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author KHighness
 * @since 2021-05-08
 */

@Slf4j(topic = "CyclicBarrier")
public class CyclicBarrierDemo {

    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {log.debug("==========================");});
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {
                try {
                    log.debug("{} first step", Thread.currentThread().getName());
                    sleep(1, TimeUnit.SECONDS);
                    cyclicBarrier.await();
                    log.debug("{} second step", Thread.currentThread().getName());
                    sleep(1, TimeUnit.SECONDS);
                    cyclicBarrier.await();
                    log.debug("{} third step", Thread.currentThread().getName());
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }
}
