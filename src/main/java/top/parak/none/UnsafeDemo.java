package top.parak.none;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author KHighness
 * @since 2021-04-28
 */

public class UnsafeDemo {
    public static void main(String[] args) throws NoSuchFieldException {
        Unsafe unsafe = UnsafeAccessor.getUnsafe();
        Field id = User.class.getDeclaredField("id");
        Field name = User.class.getDeclaredField("name");
        // 获得成员变量的偏移量
        long idOffset = unsafe.objectFieldOffset(id);
        long nameOffset = unsafe.objectFieldOffset(name);

        // 使用CAS方法替换成员变量
        User user = new User();
        unsafe.compareAndSwapInt(user, idOffset, 0, 1);
        unsafe.compareAndSwapObject(user, nameOffset, null, "KHighness");

        System.out.println(user);
    }
}

class User {
    volatile int id;
    volatile String name;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "User[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ']';
    }
}
