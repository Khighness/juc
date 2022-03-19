package top.parak.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author KHighness
 * @since 2021-09-26
 */
@Slf4j(topic = "JDKFuture")
public class JDKFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(() -> {
            log.debug("执行计算");
            TimeUnit.SECONDS.sleep(3);
            return 3;
        });
        log.debug("等待结果");
        log.debug("result = {}", future.get());
    }

}
