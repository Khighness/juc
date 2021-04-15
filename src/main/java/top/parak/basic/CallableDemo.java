package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author KHighness
 * @since 2021-04-06
 */

@Slf4j(topic = "Callable")
public class CallableDemo {

    public static class Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            log.debug("running");
            return "KHighness";
        }
    }

    public static void main(String[] args) {
        FutureTask<String> futureTask = new FutureTask<>(new Task());
        new Thread(futureTask, "t1").start();
        try {
            String res = futureTask.get();
            log.debug(res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
