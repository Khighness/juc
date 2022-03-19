package top.parak.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KHighness
 * @since 2021-05-01
 */

@Slf4j(topic = "NewFixedThreadPool")
public class NewFixedThreadPoolDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private final AtomicInteger no = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "k-pool-thread-" + no.incrementAndGet());
            }
        });
        pool.execute(() -> { log.debug("1"); });
        pool.execute(() -> { log.debug("2"); });
        pool.execute(() -> { log.debug("3"); });
        Future<Integer> future = pool.submit(() -> {
            return 1 / 0;
        });
        log.debug("{}", future.get());
    }
}
