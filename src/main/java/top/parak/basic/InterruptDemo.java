package top.parak.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-06
 */

@Slf4j(topic = "Interrupt")
public class InterruptDemo {

    @Test
    public void test1() throws InterruptedException{
        Thread t1 = new Thread(() -> {
            log.debug("sleeping");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("interrupt");
        t1.interrupt();
        TimeUnit.SECONDS.sleep(1);
        log.debug("flag => [{}]", t1.isInterrupted());
    }

    @Test
    public void test2() throws InterruptedException{
        Thread t1 = new Thread(() -> {
            log.debug("t1 running");
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    log.debug("t1 end");
                    break;
                }
            }
        }, "t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("interrupt t1");
        t1.interrupt();
        TimeUnit.SECONDS.sleep(1);
        log.debug("flag => [{}]", t1.isInterrupted());
    }
}
