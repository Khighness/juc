package top.parak.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-06
 */

@Slf4j(topic = "InterruptAPI")
public class InterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            Thread thread = Thread.currentThread();
//            while (!thread.isInterrupted()) {  // (1) isInterrupted
            while (!Thread.interrupted()) {      // (2) interrupted(static)
                log.debug("{}", thread.isInterrupted());
            }
            log.debug("{}", thread.isInterrupted());
        }, "t");
        t.start();
        TimeUnit.MILLISECONDS.sleep(1);
        t.interrupt();
    }
}
