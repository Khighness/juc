package top.parak.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author KHighness
 * @since 2021-04-27
 */

@Slf4j(topic = "Compare")
public class PerformanceCompareDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            demo(AtomicLong::new, AtomicLong::getAndIncrement);
        }
        for (int i = 0; i < 5; i++) {
            demo(LongAdder::new, LongAdder::increment);
        }
    }

    private static <T> void demo(Supplier<T> supplier, Consumer<T> consumer) {
        T adder = supplier.get();
        long start = System.nanoTime();
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            list.add(new Thread(() -> {
                for (int k = 0; k < 50_0000; k++) {
                    consumer.accept(adder);
                }
            }));
        }
        list.forEach(Thread::start);
        list.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        log.debug("{} cost: {}(ns)", adder.getClass().getSimpleName(), (end - start));
    }
}
