package top.parak.share;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author KHighness
 * @since 2021-04-09
 */

public class ClassLayoutUtil {

    private final static int FIRST_LINE_START = 185;
    private final static int LINE_LENGTH = 53;

    public static String printSimple(ClassLayout instance) {
        return instance.toPrintable().substring(FIRST_LINE_START, FIRST_LINE_START + LINE_LENGTH);
    }

}
