package top.parak.atomic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author KHighness
 * @since 2021-04-27
 */

public class DecimalAccountDemo {
    public static void main(String[] args) {
        DecimalAccount.demo(new DecimalAccountCAS(new BigDecimal("10000")));
    }
}

class DecimalAccountCAS implements DecimalAccount {
    private AtomicReference<BigDecimal> balance;

    public DecimalAccountCAS(BigDecimal balance) {
        this.balance = new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while (true) {
            BigDecimal prev = balance.get();
            BigDecimal next = prev.subtract(amount);
            if (balance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}

interface DecimalAccount {
    BigDecimal getBalance();
    void withdraw(BigDecimal amount);

    static void demo(DecimalAccount account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(BigDecimal.TEN);
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
        long end = System.nanoTime();
        System.out.printf("final balance: %s, spend time(ms): %d ",
                account.getBalance().toString(), (end - start) / 1000_000);
    }
}
