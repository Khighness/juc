package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-22
 */

public class PhilosophersEatingDemo {
    private final static int NUM = 5;
    public static void main(String[] args) {
        Chopstick[] c = new Chopstick[NUM];
        for (int i = 0; i < NUM; i++) {
            c[i] = new Chopstick(String.valueOf(i));
        }
        new Philosopher("苏格拉底", c[0], c[1]).start();
        new Philosopher("柏拉图", c[1], c[2]).start();
        new Philosopher("亚里士多德", c[2], c[3]).start();
        new Philosopher("郝拉克利特", c[3], c[4]).start();
        new Philosopher("阿基米德", c[4], c[0]).start();
    }
}

// 哲学家
@Slf4j(topic = "Philosopher")
class Philosopher extends Thread {
    final Chopstick left;  // 左手筷子
    final Chopstick right; // 右手筷子

    public Philosopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    public void run() {
        while (true) {
            // 尝试获得左手筷子
            synchronized (left) {
                // 尝试获得右手筷子
                synchronized (right) {
                    eat();
                }
            }
        }
    }

    // 吃饭
    private void eat() {
        log.debug("eating...");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// 筷子
class Chopstick {
    String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子[" + "name='" + name + ']';
    }
}
