package top.parak.tool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-05-07
 * @apiNote 交替执行
 */

@Slf4j(topic = "Alternately")
public class AlternatelyDemo {

    /**
     * 三个信号量分别控制A，B，C的打印
     */
    private static final Semaphore[] s = { new Semaphore(1), new Semaphore(1), new Semaphore(1)};
    private static final int size = 3;
    private static final char[] arr = {'A', 'B', 'C'};
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws InterruptedException {
        s[1].acquire();
        s[2].acquire();
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            executorService.submit(() -> {
                while (true) {
                    try {
                        s[finalI].acquire();
                        TimeUnit.MILLISECONDS.sleep(100);
                        log.debug("{} => [{}]", arr[finalI], System.nanoTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        s[(finalI + 1) % size].release();
                    }
                }
            });
        }
    }
}
