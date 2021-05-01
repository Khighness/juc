package top.parak.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author KHighness
 * @since 2021-05-01
 */

@Slf4j(topic = "Shutdown")
public class ShutdownDemo {
    public static void main(String[] args) /* throws InterruptedException, ExecutionException */ {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<Integer> future1 = pool.submit(() -> {
            try {
                log.debug("task1 running");
                TimeUnit.SECONDS.sleep(1);
                log.debug("task1 finished");
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
            return 1;
        });
        Future<Integer> future2 = pool.submit(() -> {
            try {
                log.debug("task2 running");
                TimeUnit.SECONDS.sleep(2);
                log.debug("task2 finished");
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
            return 2;
        });
        Future<Integer> future3 = pool.submit(() -> {
            try {
                log.debug("task3 running");
                TimeUnit.SECONDS.sleep(3);
                log.debug("task3 finished");
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
            return 3;
        });
        log.debug("shutdownNow");
        List<Runnable> tasks = pool.shutdownNow();
//        log.debug("task1 result = {}", future1.get());
//        log.debug("task2 result = {}", future2.get());
        log.debug("{}", Arrays.toString(tasks.toArray()));
    }

}

//        log.debug("shutdown");
//        pool.shutdown();
//        pool.awaitTermination(5, TimeUnit.SECONDS);
//        log.debug("something");
