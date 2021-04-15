package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * @author KHighness
 * @since 2021-04-08
 */

@Slf4j(topic = "ExerciseSell")
public class ExerciseSellDemo {
    // Random为线程安全
    static Random random = new Random();

    // 随机数1~5
    public static int randomAmount() {
        return random.nextInt(5) + 1;
    }

    public static void main(String[] args) throws InterruptedException {
        // 售票窗口，总共1000张票
        TicketWindow window = new TicketWindow(1000);
        // 线程集合，模拟多人买票
        List<Thread> threadList = new ArrayList<>();
        // 卖出的票数统计，Vector线程安全
        List<Integer> amountList = new Vector<>();
        // 模拟1000个人买票
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                // 买票
                int amount = window.sell(randomAmount());
                // 睡眠
                try {
                    Thread.sleep(randomAmount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 统计买票数
                amountList.add(amount);
            });
            threadList.add(thread);
            thread.start();
        }
        for (Thread thread : threadList) {
            thread.join();
        }
        // 查看余票数量和售票数量之和是否为1000
        log.debug("余票 => [{}]", window.getCount());
        log.debug("售票 => [{}]", amountList.stream().mapToInt(i -> i).sum());
    }
}

/**
 * 售票窗口
 */
class TicketWindow {
    // 票总数
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    // 获取余票数量
    public int getCount() {
        return count;
    }

    // 售票
    public synchronized int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}
