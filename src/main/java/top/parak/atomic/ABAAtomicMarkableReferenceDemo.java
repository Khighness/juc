package top.parak.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author KHighness
 * @since 2021-04-27
 */

@Slf4j(topic = "ABAAtomicMarkableReference")
public class ABAAtomicMarkableReferenceDemo {
    private static GarbageBag bag = new GarbageBag("装满垃圾");
    private static AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag, true);

    public static void main(String[] args) throws InterruptedException {
        log.debug("家里需要换垃圾袋...");
        GarbageBag prev = ref.getReference();
        robot();
        TimeUnit.MILLISECONDS.sleep(10);
        aunt();
        TimeUnit.MILLISECONDS.sleep(10);
        log.debug(ref.getReference().toString());
    }

    // 清洁机器人
    private static void robot() {
        new Thread(() -> {
            log.debug("清洁机器人开始打扫卫生...");
            boolean res = ref.compareAndSet(ref.getReference(), new GarbageBag("新垃圾袋"),
                    true, false);
            log.debug("机器人是否换了垃圾袋 ? {}", res);
        }, "robot").start();
    }

    // 保洁阿姨
    private static void aunt() {
        new Thread(() -> {
            log.debug("保洁阿姨开始打扫卫生...");
            bag.setDesc("空垃圾袋");
            boolean res = ref.compareAndSet(ref.getReference(), new GarbageBag("新垃圾袋"),
                    true, false);
            log.debug("阿姨是否换了垃圾袋 ? {}", res);
        }, "aunt").start();
    }
}

class GarbageBag {
    String desc;

    public GarbageBag(String desc) { this.desc = desc; }

    public void setDesc(String desc) { this.desc = desc; }

    @Override
    public String toString() { return "GarbageBag[desc='" + desc + "']"; }
}
