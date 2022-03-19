package top.parak.col;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author KHighness
 * @since 2021-06-13
 */

public class ConcurrentLinkedQueueDemo {

    public static void main(String[] args) {
        ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        concurrentLinkedQueue.offer(1);
    }
}
