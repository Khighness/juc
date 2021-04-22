package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-06
 */

@Slf4j(topic = "Join")
public class JoinDemo {

    static int i = 3;

    public static void main(String[] args) throws InterruptedException {
        update();
    }

    public static void update() throws InterruptedException {
        log.debug("main thread start");
        Thread t1 = new Thread(() -> {
            log.debug("child thread start");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("child thread end");
            i *= i;
        }, "t1"); 
        t1.start();
        t1.join();
        log.debug("i => [{}]", i);
        log.debug("main thread end");
    }


}
