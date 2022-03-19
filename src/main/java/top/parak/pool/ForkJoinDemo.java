package top.parak.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author KHighness
 * @since 2021-05-02
 */

@Slf4j(topic = "ForkJoin")
public class ForkJoinDemo {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(4);
//        System.out.println(pool.invoke(new Task(5)));
        System.out.println(pool.invoke(new AddTask(1, 5)));
    }
}

@Slf4j(topic = "Task")
class Task extends RecursiveTask<Integer> {
    private final int n;

    public Task(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "『" + n +  "』";
    }

    @Override
    protected Integer compute() {
        if (n == 1) return 1;
        Task tas = new Task(n - 1);
        tas.fork();
        log.debug("fork: {} {}", n, tas);
        int res = n + tas.join();
        log.debug("join: {} + {} = {}", n, tas, res);
        return res;
    }
}

@Slf4j(topic = "AddTask")
class AddTask extends RecursiveTask<Integer> {
    int begin;
    int end;

    public AddTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return "『" + begin + ", " + end + "』";
    }

    @Override
    protected Integer compute() {
        if (begin == end) {
            log.debug("join: {}", begin);
            return begin;
        }
        if (end - begin == 1) {
            log.debug("join: {} + {} = {}", begin, end, begin + end);
            return begin + end;
        }
        int mid = begin + (end - begin >> 1);
        AddTask task1 = new AddTask(begin, mid);
        task1.fork();
        AddTask task2 = new AddTask(mid + 1, end);
        task2.fork();
        log.debug("fork: {} + {} = ?", task1, task2);
        int res = task1.join() + task2.join();
        log.debug("join: {} + {} = {}", task1, task2, res);
        return res;
    }
}
