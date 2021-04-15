package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author KHighness
 * @since 2021-04-08
 */

@Slf4j(topic = "ExerciseTransfer")
public class ExerciseTransferDemo {
    // Random为线程安全
    static Random random = new Random();

    // 随机数1~100
    public static int randomAmount() {
        return random.nextInt(100) + 1;
    }

    public static void main(String[] args) throws InterruptedException {
        // 模拟两个账户
        Account k1 = new Account(1000);
        Account k2 = new Account(1000);
        // 互相转账1000次
        Thread t1 = new Thread(() -> {
           for (int i = 0; i < 1000; i++) {
               k1.transfer(k2, randomAmount());
           }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                k2.transfer(k1, randomAmount());
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // 查看转账2000次后的余额
        log.debug("k1 => [{}]", k1.getMoney());
        log.debug("k2 => [{}]", k2.getMoney());
    }
}

/**
 * 账户
 */
class Account {
    private int money;

    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void transfer(Account target, int amount) {
        synchronized (Account.class) {
            if (this.money >= amount) {
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }
    }
}
