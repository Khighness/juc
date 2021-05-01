package top.parak.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author KHighness
 * @since 2021-05-01
 */

@Slf4j(topic = "Starvation")
public class StarvationDemo {
    /**
     * 菜单
     */
    static final List<String> MENU = Arrays.asList("鱼香肉丝", "宫保鸡丁", "红烧肉", "烤鸡翅");
    static final Random RANDOM = new Random();
    /**
     * 随机做个菜
     */
    static String cooking() {
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }

    public static void main(String[] args) {
        // 服务员
        ExecutorService waiterPool = Executors.newFixedThreadPool(1);
        // 厨师
        ExecutorService cookPool =  Executors.newFixedThreadPool(1);
        // 10位客人
        for (int i = 0; i < 10; i++) {
            waiterPool.execute(() -> {
                log.debug("处理点餐");
                Future<String> future = cookPool.submit(() -> {
                    log.debug("做菜");
                    return cooking();
                });
                try {
                    log.debug("上菜: {}", future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
