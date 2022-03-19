package top.parak.tool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author KHighness
 * @since 2021-05-07
 */

@Slf4j(topic = "CountDownLatch")
public class CountDownLatchDemo {
    private static int total = 0;
    private static final CountDownLatch countDownLatch = new CountDownLatch(2);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    private static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        executorService.submit(() -> {
            log.debug("state = {}", countDownLatch.getCount());
            total += 1;
            sleep(1, TimeUnit.SECONDS);
            log.debug("{} run over", Thread.currentThread().getName());
            countDownLatch.countDown();
            log.debug("state = {}", countDownLatch.getCount());
        });
        executorService.submit(() -> {
            log.debug("state = {}", countDownLatch.getCount());
            total += 2;
            sleep(1, TimeUnit.SECONDS);
            log.debug("{} run over", Thread.currentThread().getName());
            countDownLatch.countDown();
            log.debug("state = {}", countDownLatch.getCount());
        });
        executorService.submit(() -> {
            log.debug("state = {}", countDownLatch.getCount());
            try {
                countDownLatch.await();
                log.debug("result: total = {}", total);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("{} run over", Thread.currentThread().getName());
        });
        executorService.shutdown();
        sleep(3, TimeUnit.SECONDS);
        executorService.shutdownNow();
    }
}
