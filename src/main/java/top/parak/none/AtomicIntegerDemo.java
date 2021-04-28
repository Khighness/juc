package top.parak.none;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KHighness
 * @since 2021-04-27
 */

public class AtomicIntegerDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        atomicInteger.updateAndGet(x -> x * 5);
        System.out.println(atomicInteger.get());
    }
}
