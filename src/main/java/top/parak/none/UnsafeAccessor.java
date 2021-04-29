package top.parak.none;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author KHighness
 * @since 2021-04-28
 */

public class UnsafeAccessor {
    private static Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static Unsafe getUnsafe() {
        return unsafe;
    }
}
