package top.parak.share;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @author KHighness
 * @since 2021-04-23
 */

public class AlternateParkUnparkDemo {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) {
        AlternateParkUnpark alternateParkUnpark = new AlternateParkUnpark(5);
        t1 = new Thread(() -> alternateParkUnpark.print("a", t2), "t1");
        t2 = new Thread(() -> alternateParkUnpark.print("b", t3), "t2");
        t3 = new Thread(() -> alternateParkUnpark.print("c", t1), "t3");
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);
    }
}

@Slf4j(topic = "AlternateParkUnpark")
class AlternateParkUnpark {
    private final int loopNumber;

    public AlternateParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Thread next) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            log.debug(str);
            LockSupport.unpark(next);
        }
    }
}
