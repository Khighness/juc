package top.parak.share;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author KHighness
 * @since 2021-04-09
 */

@Slf4j(topic = "HashcodeBiased")
public class HashcodeBiasedDemo {
    /**
     * -XX:BiasedLockingStartupDelay=0  禁用偏向锁延迟
     */
    public static void main(String[] args) {
        HashcodeBiased biased = new HashcodeBiased();
        log.debug(ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased))); // 偏向锁
        log.debug(String.valueOf(biased.hashCode()));
        log.debug(ClassLayoutUtil.printSimple(ClassLayout.parseInstance(biased))); // 无锁
    }
}

class HashcodeBiased {

}
