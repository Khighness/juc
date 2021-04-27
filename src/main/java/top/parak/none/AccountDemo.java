package top.parak.none;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author KHighness
 * @since 2021-04-26
 */

public class AccountDemo {
    public static void main(String[] args) {
        System.out.print("[Unsafe => ");
        Account a1 = new AccountUnsafe(10000);
        Account.demo(a1);
        System.out.print("[synchronized => ");
        Account a2 = new AccountSafe(10000);
        Account.demo(a2);
        System.out.print("[compareAndSet => ");
        Account a3 = new AccountCAS(10000);
        Account.demo(a3);
        System.out.println();
    }
}

class AccountUnsafe implements Account {
    // 余额
    private Integer balance;

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer geBalance() {
        return this.balance;
    }

    @Override
    public void withdraw(Integer amount) {
        this.balance -= amount;
    }
}

class AccountSafe implements Account {
    // 余额
    private Integer balance;

    public AccountSafe(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer geBalance() {
        return this.balance;
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) {
            this.balance -= amount;
        }
    }
}

class AccountCAS implements Account {
    private AtomicInteger balance;

    public AccountCAS(Integer balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer geBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        while (true) {
            // 获取余额的最新值
            int prev = this.balance.get();
            // 修改后的余额
            int next = prev - amount;
            // 同步到主存
            // CAS(预期值，修改值) => boolean(是否修改成功)
            if (this.balance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}

interface Account {
    // 获取余额
    Integer geBalance();
    // 取款
    void withdraw(Integer amount);

    /**
     * 方法内会启动1000个线程，每个线程做-10元操作
     * 如果初始余额为10000，那么正确的结果应该是0
     * @param account 账户
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        long start = System.nanoTime();
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.printf("final balance: %d, spend time(ms): %d] ", account.geBalance(), (end - start) / 1000_000);
    }
}
