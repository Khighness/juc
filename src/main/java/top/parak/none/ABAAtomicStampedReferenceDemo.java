package top.parak.none;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author KHighness
 * @since 2021-04-27
 */

@Slf4j(topic = "ABAAtomicStampedReference")
public class ABAAtomicStampedReferenceDemo {
   private static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        int stamp = ref.getStamp();
        other();
        TimeUnit.SECONDS.sleep(1);
        boolean res = ref.compareAndSet(ref.getReference(), "K", stamp, stamp + 1);
        log.debug("change A -> K ? {}", res);
    }

    private static void other() {
        new Thread(() -> {
            int stamp = ref.getStamp();
            boolean res = ref.compareAndSet(ref.getReference(), "B", stamp, stamp + 1);
            log.debug("change A -> B ? {}", res);
        }, "B").start();
        new Thread(() -> {
            int stamp = ref.getStamp();
            boolean res = ref.compareAndSet(ref.getReference(), "A", stamp, stamp + 1);
            log.debug("change B -> A ? {}", res);
        }, "A").start();
    }

}
