package top.parak.basic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-04-05
 */

@Slf4j(topic = "PC")
public class MachineDemo {

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        log.debug("CPU => [{}]", runtime.availableProcessors());
        log.debug("totalMemory => [{}]", runtime.totalMemory());
        log.debug("maxMemory => [{}]", runtime.maxMemory());
    }
}
