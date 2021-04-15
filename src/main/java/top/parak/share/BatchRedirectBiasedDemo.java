package top.parak.share;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;

/**
 * @author KHighness
 * @since 2021-04-10
 */

@Slf4j(topic = "BatchRedirectBiased")
public class BatchRedirectBiasedDemo {

    public static void main(String[] args) {
        Vector<BatchBiased> list = new Vector<>();
        new Thread(() -> {
           for (int i = 0; i < 30; i++) {
               BatchBiased biased = new BatchBiased();
               list.add(biased);
               synchronized (biased) {
                   log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased))); // 偏向锁，偏向t1
               }
           }
           synchronized (list) {
               list.notify();
           }
        }, "t1").start();
        new Thread(() -> {
            synchronized (list) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("==========>");
            for (int i = 0; i < 30; i++) {
                BatchBiased biased = list.get(i);
                log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));     // 无锁
                synchronized (biased) {
                    log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased))); // 偏向锁
                }
                log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));
            }
        }, "t2").start();
    }
}

class BatchBiased {

}
