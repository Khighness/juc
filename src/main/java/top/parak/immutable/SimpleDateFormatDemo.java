package top.parak.immutable;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author KHighness
 * @since 2021-04-29
 */

@Slf4j(topic = "SimpleDateFormat")
public class SimpleDateFormatDemo {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static void main(String[] args) {
        String dateStr = "2001-09-11 00:00:00.000";
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    synchronized (SDF) {
                        log.debug(SDF.parse(dateStr).toString());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
