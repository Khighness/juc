package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-04-06
 */

@Slf4j(topic = "Runnable")
public class RunnableDemo {

    public static void main(String[] args) {
        Runnable r1 = () -> log.debug("running");
        new Thread(r1, "t1").start();
    }
}
