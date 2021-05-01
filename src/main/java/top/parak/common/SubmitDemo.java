package top.parak.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author KHighness
 * @since 2021-05-01
 */

@Slf4j(topic = "Submit")
public class SubmitDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        method3();
    }

    public static void method1() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<String> future = pool.submit(() -> {
            log.debug("running");
            TimeUnit.SECONDS.sleep(1);
            return "OK";
        });
        log.debug("{}", future.get());
    }

    public static void method2() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<Future<Object>> futures = pool.invokeAll(Arrays.asList(
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(1);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(2);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(3);
                    return "3";
                }
        ));
        futures.forEach(f -> {
            try {
                log.debug("{}", f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public static void method3() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        String s = pool.invokeAny(Arrays.asList(
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(1);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(2);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(3);
                    return "3";
                }
        ));
        log.debug(s);
    }
}
