package top.parak.col;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author KHighness
 * @since 2021-06-11
 */

public class CopyOnWriteListDemo {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Hello");
        Thread thread = new Thread(() -> {
            list.add("KHighness");
        });
        Iterator<String> iterator = list.iterator();
        thread.start();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
