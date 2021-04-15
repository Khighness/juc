package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "BoilWaterToMakeTea")
public class PlanDemo {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("洗水壶");
            sleep(60);      // 洗水壶 1分钟
            log.debug("烧开水");
            sleep(60 * 5);  // 烧开水 5分钟
        }, "t1");
        Thread t2 = new Thread(() -> {
            log.debug("洗茶壶");
            sleep(60);      // 洗水壶 1分钟
            log.debug("洗茶杯");
            sleep(60 * 2);  // 洗茶杯 2分钟
            log.debug("拿茶叶");
            sleep(60 );     // 烧开水 1分钟
            try {
                t1.join();               // 等待t1烧水
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("泡茶");
        }, "t2");
        t1.start();
        t2.start();
    }

    public static void sleep(int nanoSeconds) {
        try {
            TimeUnit.NANOSECONDS.sleep(nanoSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
