package top.parak.tool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-05-07
 * @apiNote 同时执行
 */

@Slf4j(topic = "Simultaneously")
public class SimultaneouslyDemo {
    private static final int size = 3;
    private static final char[] arr = {'A', 'B', 'C'};
    private static final CountDownLatch countDownLatch = new CountDownLatch(3);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(size);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < size; i++) {
            int finalI = i;
            executorService.submit(() -> {
                countDownLatch.countDown();
                log.debug("{} => [{}]", arr[finalI], System.nanoTime());
            });
        }
        TimeUnit.SECONDS.sleep(1);
        countDownLatch.await();
        executorService.shutdown();
    }
}
