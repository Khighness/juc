package top.parak.jmm;

import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-04-25
 */

@Slf4j(topic = "BalkingPractice")
public class BalkingPractice {
    static volatile boolean initialized = false;
    final static Object lock = new Object();

    private static void init() {
        synchronized (lock) {
            if (initialized) {
                return;
            }
            doInit();
            initialized = true;
        }
    }

    private static void doInit() {
        log.debug("init...");
    }
}
