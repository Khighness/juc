package top.parak.none;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KHighness
 * @since 2021-04-27
 */

@Slf4j(topic = "LockCAS")
public class LockCASDemo {
    // 0表示没加锁
    // 1表示加了锁
    private final AtomicInteger state = new AtomicInteger(0);

    private void lock() {
        while (true) {
            if (state.compareAndSet(0, 1)) {
                break;
            }
        }
    }

    public void unlock() {
        log.debug("unlock...");
        state.set(0);
    }
}
