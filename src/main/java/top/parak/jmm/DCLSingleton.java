package top.parak.jmm;

/**
 * @author KHighness
 * @since 2021-04-24
 */

public class DCLSingleton {
    private DCLSingleton() { }
    private volatile static DCLSingleton INSTANCE = null;
    /**
     * 在第一次线程调用getInstance()，直接在synchronized外，判断instance对象是否存在
     * 如果不存在，才会去获取锁，然后创建单例对象，并且返回；第二个线程调用getInstance()，
     * 会进行instance的空判断，如果已经有单例对象就不会去同步块中获取锁，提高效率。
     */
    private static DCLSingleton getInstance() {
        if (INSTANCE == null) {
            // 这个判断并不在synchronized同步代码块中，
            // 不能保证原子性、可见性和有序性，可能导致指令重排
            synchronized (DCLSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DCLSingleton();
                }
            }
        }
        return INSTANCE;
    }
}
