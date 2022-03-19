package top.parak.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KHighness
 * @since 2021-05-01
 */

@Slf4j(topic = "NewSingleThreadExecutor")
public class NewSingleThreadExecutorDemo {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newSingleThreadExecutor(new ThreadFactory() {
            private final AtomicInteger no = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "k-pool-thread-" + no.incrementAndGet());
            }
        });
        pool.execute(() -> {
            log.debug("1");
            int i = 1 / 0;
        });
        pool.execute(() -> { log.debug("2"); });
        pool.execute(() -> { log.debug("3"); });
    }
}
