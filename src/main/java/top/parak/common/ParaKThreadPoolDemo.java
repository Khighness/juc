package top.parak.common;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author KHighness
 * @since 2021-04-30
 */

@Slf4j(topic = "ParaKThreadPoolDemo")
public class ParaKThreadPoolDemo {
    public static void main(String[] args) {
        ParaKThreadPool threadPool = new ParaKThreadPool(
                1, 1000, TimeUnit.MILLISECONDS, 1,
                // (1) 一直等待
                // BlockingQueue::put
//                (2) 超时等待
                (queue, task) -> {
                    queue.offer(task, 1500, TimeUnit.MILLISECONDS);
                }
                // (3) 放弃执行
//                (queue, task) -> {
//                    log.debug("放弃执行: {}", task);
//                }
                // (4) 抛出异常
//                (queue, task) -> {
//                    throw new RuntimeException("任务执行失败: " + task);
//                }
                // (5) 自己执行
//                (queue, task) -> {
//                    task.run();
//                }
        );
        for (int i = 0; i < 3; i++) {
            int k = i;
            threadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("{}", k);
            });
        }
    }
}

@Slf4j(topic = "ParaKThreadPool")
class ParaKThreadPool {
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;

    // 线程集合
    private final HashSet<Worker> workers = new HashSet<>();

    // 核心线程数
    private final int coreSize;

    // 获取任务的超时时间
    private final long timeout;

    // 时间单位
    private final TimeUnit timeUnit;

    // 拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    // 构造函数
    public ParaKThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    // 执行任务
    public void execute(Runnable task) {
        // 当任务数没有超过coreSize时，直接交给worker对象执行
        // 如果任务数超过coreSize时，加入任务队列暂存
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("执行线程新增: {}", worker);
                workers.add(worker);
                worker.start();
            } else {
                // (1) 一直等待
                // (2) 超时等待
                // (3) 放弃执行
                // (4) 抛出异常
                // (5) 让调用者自己执行任务
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // (1) 当task不为空，执行任务
            // (2) 当task执行完毕，再接着从任务队列获取任务并执行
//            while (task != null || (task = taskQueue.take()) != null) {
            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
                try {
                    log.debug("正在执行 => {}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("执行线程移除: {}", this);
                workers.remove(this);
            }
        }
    }

    public static void main(String[] args) {
    }
}

@Slf4j(topic = "BlockingQueue")
class BlockingQueue<T> {

    // 任务队列
    private final Deque<T> queue = new ArrayDeque<>();

    // 锁
    private final ReentrantLock lock = new ReentrantLock();

    // 最大容量
    private final int capacity;

    // 生产者条件变量
    private final Condition fullWaitSet = lock.newCondition();

    // 消费者条件变量
    private final Condition emptyWaitSet = lock.newCondition();

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    // 带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            // 将超时时间统一转换为纳秒
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    // 返回剩余时间
                    if (nanos <= 0) {
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞获取
    public T take() {
        lock.lock();
        try {
            // 队列为空，阻塞
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞添加
    public void put(T element) {
        lock.lock();
        try {
            // 队列已满，阻塞
            while (queue.size() == capacity) {
                log.debug("等待加入任务队列: {}...", element);
                fullWaitSet.await();
            }
            log.debug("加入任务队列: {}", element);
            queue.addLast(element);
            emptyWaitSet.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // 带超时时间添加
    public boolean offer(T element, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            // 队列已满，阻塞
            while (queue.size() == capacity) {
                if (nanos <= 0) {
                    return false;
                }
                log.debug("等待加入任务队列: {}...", element);
                try {
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列: {}", element);
            queue.addLast(element);
            emptyWaitSet.signal();
            return true;
        }finally {
            lock.unlock();
        }
    }

    // 获取大小
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            // 判断队列是否已满
            if (queue.size() == capacity) {
                rejectPolicy.reject(this, task);
            } else { // 有空闲
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}

@FunctionalInterface
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}
