package top.parak.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-05-01
 */

@Slf4j(topic = "Timer")
public class TimerDemo {
    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task1");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task2");
            }
        };
        log.debug("start...");
        timer.schedule(task1, 1000);
        timer.schedule(task2, 1000);
    }
}
