package top.parak.share;

import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-04-08
 */

@Slf4j(topic = "Synchronized")
public class SynchronizedDemo {

    static final Object lock = new Object();
    static int counter = 0;

    public static void main(String[] args) {
        synchronized (lock) {
            counter++;
        }
    }
}
