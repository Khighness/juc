package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author KHighness
 * @since 2021-04-13
 */

@Slf4j(topic = "ParkUnpark")
public class ParkUnparkDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str  = null;
        while (!(str = sc.nextLine()).equals("")) {
            String[] s = str.split(" ");
            int time1 = Integer.parseInt(s[0]);
            int time2 = Integer.parseInt(s[1]);

            // park
            Thread t1 = new Thread(() -> {
                log.debug("start...");
                try {
                    TimeUnit.SECONDS.sleep(time1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("park...");
                LockSupport.park();
                log.debug("continue...");
            }, "t1");
            t1.start();
            // unpark
            try {
                TimeUnit.SECONDS.sleep(time2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // unpark既可以在park之前调用，也可以在park之后调用
            log.debug("unpark...");
            LockSupport.unpark(t1);
        }
    }
}
