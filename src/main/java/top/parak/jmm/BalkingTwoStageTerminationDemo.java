package top.parak.jmm;

import javafx.scene.input.TouchEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-24
 */

public class BalkingTwoStageTerminationDemo {
    public static void main(String[] args) throws InterruptedException {
        BalkingTwoStageTermination btst = new BalkingTwoStageTermination();
        btst.start();
        TimeUnit.SECONDS.sleep(3);
        btst.stop();
        TimeUnit.SECONDS.sleep(1);
        btst.start();
        TimeUnit.SECONDS.sleep(3);
        btst.stop();
    }
}

@Slf4j(topic = "BalkingTwoStageTermination")
class BalkingTwoStageTermination {
    // 监控线程
    private Thread monitorThread;
    // 是否被打断
    private volatile boolean stop = false;
    // 是否已执行
    private volatile boolean started = false;

    // 启动
    public void start() {
        synchronized (this) {
            if (started) {
                return;
            }
            stop = false;
            started = true;
        }
        monitorThread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                // 是否被打断
                if (stop) {
                    log.debug("停止监控");
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("执行监控...");
            }
        }, "monitor");
        monitorThread.start();
    }

    // 暂停
    public void stop() {
        stop = true;
        started = false;
    }
}
