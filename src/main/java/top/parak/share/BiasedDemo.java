package top.parak.share;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-09
 */

@Slf4j(topic = "Biased")
public class BiasedDemo {

    /**
     * -XX:BiasedLockingStartupDelay=0  禁用偏向锁延迟
     * -XX:-UseBiasedLocking            禁用偏向锁
     */
    public static void main(String[] args) throws InterruptedException {
        Biased biased = new Biased();
        log.debug("Before Biased Lock");
        log.debug(ClassLayout.parseInstance(biased).toPrintable());     // 无锁
        TimeUnit.SECONDS.sleep(3);
        synchronized (biased) {
            log.debug(ClassLayout.parseInstance(biased).toPrintable()); // 偏向锁
        }
        log.debug("After Biased Lock");
        log.debug(ClassLayout.parseInstance(biased).toPrintable());     // 偏向锁
    }
}

class Biased {

}
