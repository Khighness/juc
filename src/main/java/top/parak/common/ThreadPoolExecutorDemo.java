package top.parak.common;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KHighness
 * @since 2021-05-01
 */

@Slf4j(topic = "ThreadPoolExecutor")
public class ThreadPoolExecutorDemo {
    static AtomicInteger threadId = new AtomicInteger(0);

    public static void main(String[] args) {
        // 有界阻塞队列
        ArrayBlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(10);
        // 创建线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "pool_thread_" + threadId.incrementAndGet());
            }
        };

        // 手动创建线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 10,
                TimeUnit.SECONDS, blockingQueue, threadFactory, new ThreadPoolExecutor.AbortPolicy());

        // 执行20个任务
        for (int i = 0; i < 20; i++) {
            executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.debug(Thread.currentThread().toString());
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            );
        }
    }
}
