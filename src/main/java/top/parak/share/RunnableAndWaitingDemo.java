package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-19
 */

@Slf4j(topic = "RunnableAndWaiting")
public class RunnableAndWaitingDemo {

    final static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行...");
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其他代码...");
            }
        }, "t1").start();
        new Thread(() -> {
            synchronized (obj) {
                log.debug("执行...");
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("其他代码...");
            }
        }, "t2").start();

        TimeUnit.MILLISECONDS.sleep(500);
        log.debug("唤醒obj上的其他线程");
        synchronized (obj) {
            obj.notifyAll();
        }
    }
}
