package top.parak.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author KHighness
 * @since 2021-05-03
 * @apiNote 生产-消费模型
 */

@Slf4j(topic = "ProducerConsumerModel")
public class ProducerConsumerModel {
    /** 不可重入独占锁 */
//    private final static ParaKLock LOCK = new ParaKLock();
    private final static ReentrantLock LOCK = new ReentrantLock();
    /** 已满：生产者条件变量 */
    private final static Condition FULL = LOCK.newCondition();
    /** 已空：消费者者条件变量 */
    private final static Condition EMPTY = LOCK.newCondition();
    /** 产品队列 */
    private final static Queue<Character> QUEUE = new LinkedBlockingDeque<>();
    /** 最大容量 */
    private final static int QUEUE_MAX_SIZE = 5;
    /** 随机变量 */
    private final static Random RANDOM = new Random();

    /**
     * 生成随机数字/字母字符
     * ascii  char
     * 48-57  0~9
     * 65-90  A~Z
     * 97-122 a~z
     * @return ascii码字符
     */
    private static Character randomChar() {
        int choice = RANDOM.nextInt(3);
        if (choice == 0)
            return (char) (RANDOM.nextInt(10) + 48);
        else if (choice == 1)
            return (char) (RANDOM.nextInt(26) + 65);
         else
            return (char) (RANDOM.nextInt(26) + 97);
    }

    /**
     * 生产者线程
     */
    static class Producer extends Thread {
        public Producer() {
            super.setName("Producer");
        }

        @Override
        public void run() {
            while (true) {
                // 获取独占锁
                LOCK.lock();
                try {
                    // (1) 如果队列已满，等待
                    while (QUEUE.size() == QUEUE_MAX_SIZE) {
                        FULL.await();
                    }
                    // (2) 生产一个元素到队列
                    Character character = randomChar();
                    TimeUnit.SECONDS.sleep(1);
                    log.debug("生产 => [{}]", character);
                    QUEUE.add(character);
                    // (3) 唤醒消费者线程
                    EMPTY.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放独占锁
                    LOCK.unlock();
                }
            }
        }
    }

    /**
     * 消费者线程
     */
    static class Consumer extends Thread {
        public Consumer() {
            super.setName("Consumer");
        }

        @Override
        public void run() {
            while (true) {
                // 获取独占锁
                LOCK.lock();
                try {
                    // (1) 如果队列为空，等待
                    while (QUEUE.isEmpty()) {
                        EMPTY.await();
                    }
                    // (2) 消费一个元素
                    Character character = QUEUE.poll();
                    TimeUnit.SECONDS.sleep(1);
                    log.debug("消费 => [{}]", character);
                    // (3) 唤醒生产者线程
                    FULL.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LOCK.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.start();
        consumer.start();
    }
}
