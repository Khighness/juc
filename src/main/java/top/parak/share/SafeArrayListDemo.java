package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * @author KHighness
 * @since 2021-04-07
 */


@Slf4j(topic = "SafeArrayList")
public class SafeArrayListDemo {
    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        SafeOperation operation = new SafeOperation();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> operation.method1(LOOP_NUMBER), "Thread" + (i + 1)).start();
        }
    }
}

class SafeOperation {

    public void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    public void method2(ArrayList<String> list) { list.add("1"); }

    public void method3(ArrayList<String> list) { list.remove(0); }
}

class SubSafeOperation extends SafeOperation{
    @Override
    public void method3(ArrayList<String> list) {
        new Thread(() -> list.remove(0)).start();
    }
}
