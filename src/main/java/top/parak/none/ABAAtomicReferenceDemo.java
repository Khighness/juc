package top.parak.none;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author KHighness
 * @since 2021-04-27
 */

@Slf4j(topic = "ABAAtomicReference")
public class ABAAtomicReferenceDemo {
    private static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        String prev = ref.get();
        other();
        TimeUnit.SECONDS.sleep(1);
        log.debug("change A -> K ? {}", ref.compareAndSet(prev, "K"));
    }

    private static void other() {
        new Thread(() -> log.debug("change A -> B ? {}", ref.compareAndSet(ref.get(), "B")), "B").start();
        new Thread(() -> log.debug("change B -> A ? {}", ref.compareAndSet(ref.get(), "A")), "A").start();
    }
}
