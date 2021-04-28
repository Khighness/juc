package top.parak.none;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author KHighness
 * @since 2021-04-27
 */

@Slf4j(topic = "AtomicFieldUpdater")
public class AtomicFieldUpdaterDemo {
    public static void main(String[] args) {
        Student stu = new Student();
        AtomicReferenceFieldUpdater updater = AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");
        log.debug("update ? {}", updater.compareAndSet(stu, null, "RubbishK"));
        log.debug("update ? {}", updater.compareAndSet(stu, stu.getName(), "FlowerK"));
        log.debug(stu.toString());
    }
}

class Student {
    String name;

    public String getName() { return name; }

    @Override
    public String toString() { return "Student[name='" + name + "']"; }
}
