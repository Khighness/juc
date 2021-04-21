package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-22
 */

public class MultiLockDemo {
    public static void main(String[] args) {
        BigRoom bigRoom = new BigRoom();
        new Thread(() -> {
            try {
                bigRoom.study();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "小南").start();
        new Thread(() -> {
            try {
                bigRoom.sleep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "小女").start();
    }
}

@Slf4j(topic = "BigRoom")
class BigRoom {
    /* 书房 */
    private final Object studyRoom = new Object();
    /* 卧室 */
    private final Object sleepRoom = new Object();
    public void sleep() throws InterruptedException {
        synchronized (sleepRoom) {
            log.debug("sleep2个小时");
            TimeUnit.SECONDS.sleep(2);
        }
    }

    public void study() throws InterruptedException {
        synchronized (studyRoom) {
            log.debug("sleep1个小时");
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
