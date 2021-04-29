package top.parak.atomic;

import sun.misc.Unsafe;

/**
 * @author KHighness
 * @since 2021-04-28
 */

public class KAtomicIntegerDemo {
    public static void main(String[] args) {
        Account.demo(new KAtomicInteger(10000));
    }
}

class KAtomicInteger implements Account{
    private volatile int value;

    private static long valueOffset;

    private static final Unsafe UNSAFE;

    static {
        UNSAFE = UnsafeAccessor.getUnsafe();
        try {
            valueOffset = UNSAFE.objectFieldOffset(KAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public KAtomicInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void decrement(int amount) {
        while (true) {
            int prev = this.value;
            int next = prev - amount;
            if (UNSAFE.compareAndSwapInt(this, valueOffset, prev, next)) {
                break;
            }
        }
    }

    @Override
    public Integer geBalance() {
        return getValue();
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }
}
