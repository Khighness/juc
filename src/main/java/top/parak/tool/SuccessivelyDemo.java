package top.parak.tool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-05-07
 * @apiNote 依次执行
 */

@Slf4j(topic = "Successively")
public class SuccessivelyDemo {

    /**
     * <li>resource = 0, print A</li>
     * <li>resource = 1, print B</li>
     * <li>resource = 2, print C</li>
     */
    private static volatile int resource = 0;
    private static final int size = 3;
    private static final char[] arr = {'A', 'B', 'C'};
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            executorService.submit(() -> {
                while (true) {
                    if (resource == finalI) {
                        // 此处用{@code System.out.println()}，底层是synchronized，就没必要给resource加上volatile
                        log.debug("{} => [{}]", arr[finalI], System.nanoTime());
                        resource = (resource + 1) % size;
                        break;
                    }
                }
            });
        }
        TimeUnit.SECONDS.sleep(1);
        executorService.shutdown();
    }
}
