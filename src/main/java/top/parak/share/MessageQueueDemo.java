package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-13
 */

public class MessageQueueDemo {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);
        // 3个生产者
        for (int i = 0; i < 3; i++) {
            final int id = i + 1;
            new Thread(() -> {
                queue.put(new Message(id, "值" + id));
            }, "生产者" + id).start();
        }
        // 1个消费者
        new Thread(() -> {
            while (true) {
                // 1S消费一个线程
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.get();
            }
        }, "消费者").start();
    }
}

// 消息队列类
@Slf4j(topic = "MessageDemo")
class MessageQueue {
    // 消息队列集合
    private LinkedList<Message> list;
    // 消息队列容量
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
        this.list = new LinkedList<>();
    }

    // 获取消息
    public Message get() {
        // 检查队列是否为空
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，消费者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 从队列头部获取消息并返回
            Message message = list.removeFirst();
            log.debug("消费消息 <= {}", message.toString());
            list.notifyAll();
            return message;
        }
    }

    // 存入消息
    public void put(Message message) {
        synchronized (list) {
            // 检查队列是否已满
            while (list.size() == capacity) {
                try {
                    log.debug("队列已满，生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 将消息加入队列尾部
            list.addLast(message);
            log.debug("生产消息 => {}", message.toString());
            list.notifyAll();
        }
    }
}

// 消息
class Message {
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message[" +
                "id=" + id +
                ", value=" + value +
                ']';
    }
}
