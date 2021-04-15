package top.parak.share;

import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "AddSub")
public class AddSubDemo {

    static Integer counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                counter++;
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                counter--;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("counter = {}", counter);
    }
}
