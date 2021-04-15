package top.parak.share;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author KHighness
 * @since 2021-04-09
 */

@Slf4j(topic = "OtherThreadBiased")
public class OtherThreadBiasedDemo {
    /**
     * -XX:BiasedLockingStartupDelay=0  禁用偏向锁延迟
     */
    public static void main(String[] args) {
        OtherThreadBiased biased = new OtherThreadBiased();
        new Thread(() -> {
            log.debug(ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));     //  偏向锁
            synchronized (biased) {
                log.debug(ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased))); // 偏向锁
            }
            log.debug(ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));     // 偏向锁
            synchronized (OtherThreadBiased.class) {
                OtherThreadBiased.class.notify();
            }
        }, "t1").start();
        new Thread(() -> {
            synchronized (OtherThreadBiased.class) {
                try {
                    OtherThreadBiased.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug(ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));      // 偏向锁
            synchronized (biased) {
                log.debug(ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));  // 偏向锁
            }
            log.debug(ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased)));      // 无锁
        }, "t2").start();
    }
}

class OtherThreadBiased {

}
