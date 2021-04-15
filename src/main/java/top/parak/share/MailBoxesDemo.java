package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-04-11
 */

public class MailBoxesDemo {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new People().start();
        }
        TimeUnit.SECONDS.sleep(1);
        for (Integer id : MailBoxes.getIds()) {
            new Postman(id, "内容" + id).start();
        }
    }
}

// 居民
@Slf4j(topic = "People")
class People extends Thread {
    @Override
    public void run() {
        Mail mail = MailBoxes.createMail();
        log.debug("开始收信: {}", mail.getId());
        String content = (String) mail.getContent(5000);
        log.debug("收到信件: {}",  content);
    }
}

// 邮递员
@Slf4j(topic = "Postman")
class Postman extends Thread{
    private int id;
    private String content;

    public Postman(int id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public void run() {
        Mail mail = MailBoxes.getMail(id);
        log.debug("送信 [id={}, content={}]", id, content);
        mail.setContent(content);
    }
}

// 信箱
class MailBoxes {
    private static Map<Integer, Mail> boxes = new Hashtable<>();

    private static int id = 0;

    private static synchronized int generateId() {
        return ++id;
    }

    public static Mail getMail(int id) {
        return boxes.remove(id);
    }

    public static Mail createMail() {
        int id = generateId();
        Mail mail = new Mail(id);
        boxes.put(id, mail);
        return mail;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}

// 信件
class Mail {
    // 信件ID
    private final int id;

    public Mail(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // 信件内容
    private Object content;

    // 获取内容
    public Object getContent(long timeout) {
        synchronized (this) {
            // 开始时间
            long start = System.currentTimeMillis();
            // 经历时间
            long pass = 0;
            // 没有结果
            while (content == null) {
                // 此轮循环应该等待的时间
                long wait = timeout - pass;
                if (wait <= 0) {
                    break;
                }
                try {
                    this.wait(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pass = System.currentTimeMillis() - start;
            }
            return content;
        }
    }

    // 设置内容
    public void setContent(Object content) {
        synchronized (this) {
            this.content = content;
            this.notifyAll();
        }
    }
}
