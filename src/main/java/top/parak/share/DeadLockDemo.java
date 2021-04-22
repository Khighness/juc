package top.parak.share;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-22
 */

public class DeadLockDemo {
    public static void main(String[] args) {
        final Object A = new Object();
        final Object B = new Object();
        new Thread(() -> {
            synchronized (A) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {

                }
            }
        }, "1");
        new Thread(() -> {
            synchronized (B) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A) {

                }
            }
        }, "2");
    }
}
