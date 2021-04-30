package top.parak.immutable;

import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @author KHighness
 * @since 2021-04-29
 */

@Slf4j(topic = "DateTimeFormatter")
public class DateTimeFormatterDemo {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static void main(String[] args) {
        String dateStr = "2001-09-11 00:00:00.000";
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                TemporalAccessor date = DTF.parse(dateStr);
                log.debug(date.toString());
            }).start();
        }
    }
}
