package top.parak.share;

import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-04-23
 */

public class AlternateWaitNotifyDemo {
    public static void main(String[] args) {
        AlternateWaitNotify alternateWaitNotify = new AlternateWaitNotify(1, 5);
        new Thread(() -> alternateWaitNotify.print("a", 1, 2), "A").start();
        new Thread(() -> alternateWaitNotify.print("b", 2, 3), "B").start();
        new Thread(() -> alternateWaitNotify.print("c", 3, 1), "C").start();
    }
}

@Slf4j(topic = "AlternateWaitNotify")
class AlternateWaitNotify {
    // 当前标记
    private int flag;
    // 循环次数
    private final int loopNumber;

    public AlternateWaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    /**
     *   输出内容   等待标记   下个标记
     *      a         1         2
     *      b         2         3
     *      c         3         1
     *
     * @param str       输出内容
     * @param waitFlag  等待标记
     * @param nextFlag  下个标记
     */
    public void print(String str, int waitFlag, int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while (waitFlag != this.flag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug(str);
                this.flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}
