package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-04-06
 */

@Slf4j(topic = "Thread")
public class ThreadDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
           log.debug("running");
        }, "t1");
        t1.start();
    }
}
