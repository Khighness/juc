package top.parak.share;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;
import java.util.concurrent.locks.LockSupport;

/**
 * @author KHighness
 * @since 2021-04-10
 */

@Slf4j(topic = "BatchRevokeBiased")
public class BatchRevokeBiasedDemo {

    static Thread t1, t2, t3;

    public static void main(String[] args) throws InterruptedException {
        Vector<BatchRevokeBiased> list = new Vector<>();
        int loopNumber = 39;
        t1 = new Thread(() -> {
            for (int i = 0;  i < loopNumber; i++) {
                BatchRevokeBiased biased = new BatchRevokeBiased();
                list.add(biased);
                synchronized (biased) {
                    log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));
                }
            }
            LockSupport.unpark(t2);
        }, "t1");
        t1.start();
        t2 = new Thread(() -> {
            LockSupport.park();
            log.debug("==========>");
            for (int i = 0; i < loopNumber; i++) {
                BatchRevokeBiased biased = list.get(i);
                log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));
                synchronized (biased) {
                    log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));
                }
                log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));
            }
            LockSupport.unpark(t3);
        }, "t2");
        t2.start();
        t3 = new Thread(() -> {
            LockSupport.park();
            log.debug("==========>");
            for (int i = 0; i < loopNumber; i++) {
                BatchRevokeBiased biased = list.get(i);
                log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));
                synchronized (biased) {
                    log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));
                }
                log.debug("{} => {}", i, ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));
            }
        }, "t3");
        t3.start();
        t3.join();
        log.debug("==========>");
        log.debug(ClassLayoutUtil.printSimple(ClassLayout.parseInstance(new BatchRevokeBiasedDemo())));
    }
}

class BatchRevokeBiased {

}
