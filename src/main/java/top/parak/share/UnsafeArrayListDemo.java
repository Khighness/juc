package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * @author KHighness
 * @since 2021-04-07
 */

@Slf4j(topic = "UnsafeArrayList")
public class UnsafeArrayListDemo {
    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        UnsafeOperation operation = new UnsafeOperation();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> operation.method1(LOOP_NUMBER), "Thread" + (i + 1)).start();
        }
    }
}

class UnsafeOperation {
    ArrayList<String> list = new ArrayList<>();

    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            // { 临界区，会产生竞态条件
                method2();
                method3();
            // } 临界区
        }
    }

    public void method2() { list.add("1"); }

    public void method3() { list.remove(0); }
}
