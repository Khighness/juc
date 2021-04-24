package top.parak.jmm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-23
 */

public class TwoStageTerminationDemo {
    public static void main(String[] args) throws InterruptedException {
        TwoStageTermination tst = new TwoStageTermination();
        tst.start();
        Thread.sleep(3500);
        tst.stop();
    }
}

@Slf4j(topic = "TwoStageTermination")
class TwoStageTermination {
    // 监控线程
    private Thread monitorThread;
    // 是否被打断
    private volatile boolean stop = false;

    public void start() {
        monitorThread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                // 是否被打断
                if (stop) {
                    log.debug("料理后事");
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

    public void stop() {
        stop = true;
    }
}
