package top.parak.none;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author KHighness
 * @since 2021-04-27
 */

@Slf4j(topic = "AtomicArray")
public class AtomicArrayDemo {
    public static void main(String[] args) {
        demo(
                () -> new int[10],
                array -> array.length,
                (array, index) -> array[index]++,
                array ->  log.debug("普通数组：{}", Arrays.toString(array))
        );
        demo(
                () -> new AtomicIntegerArray(10),
                AtomicIntegerArray::length,
                AtomicIntegerArray::getAndIncrement,
                array -> log.debug("安全数组：{}", array)
        );
    }

    /**
     * @param arraySupplier   提供数组
     * @param lengthFunction  获取数组长度的方法
     * @param putConsumer     指定元素的自增方法
     * @param printConsumer   打印数组元素的方法
     * @apiNote
     * <p> Supplier 提供者 无中生有         () -> 结果 </p>
     * <p> Function  函数  一个参数一个结果 (参数) -> 结果  | BiFunction (参数1，参数2) -> 结果 </p>
     * <p> Consumer 消费者 一个参数没有结果 (参数) -> Void  | BiConsumer (参数1，参数2) -> Void </p>
     */
    private static <T> void demo(Supplier<T> arraySupplier, Function<T, Integer> lengthFunction,
            BiConsumer<T, Integer> putConsumer, Consumer<T> printConsumer) {
        List<Thread> list = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFunction.apply(array);

        for (int i = 0; i < length; i++) {
            list.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) { // 正确结果应该是数组元素都为10000
                    putConsumer.accept(array, j % length);
                }
            }));
        }

        list.forEach(Thread::start);
        list.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        printConsumer.accept(array);
    }
}
