package top.parak.pool;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-05-02
 */

@Slf4j(topic = "Schedule")
public class ScheduleDemo {
    private static final ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        // 每周六15：16：00打印KHighness
        solution(DayOfWeek.SUNDAY, 15, 16, 00, () -> { log.debug("KHighness"); });
    }

    /**
     * 指定时间执行任务
     *
     * @param day     周几
     * @param hour    几时
     * @param minute  几分
     * @param second  几秒
     * @param task    任务
     */
    public static void solution(DayOfWeek day, int hour, int minute, int second, Runnable task) {
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 目标时间
        LocalDateTime tar = now.withHour(hour).withMinute(minute).withSecond(second).with(day);
        // 如果当前时间 > 目标时间，则为下周
        if (now.compareTo(tar) > 0) {
            tar.plusWeeks(1);
        }
        // 间隔时间
        long initialDelay = Duration.between(now, tar).toMillis();
        // 循环周期
        long period = 7 * 24 * 3600 * 1000;
        // 执行任务
        pool.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }
}
