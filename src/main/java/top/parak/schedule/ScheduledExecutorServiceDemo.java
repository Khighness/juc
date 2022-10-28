package top.parak.schedule;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author KHighness
 * @since 2022-10-28
 * @email parakovo@gmail.com
 */
@Slf4j(topic = "ScheduledExecutorServiceDemo")
public class ScheduledExecutorServiceDemo {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "scheduler"));

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        log.info("start");
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            log.info("K");
        }, 1, 3, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(10);
        scheduledFuture.cancel(false);
        scheduledExecutorService.shutdown();
    }
}
