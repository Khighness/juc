package top.parak.immutable;

/**
 * @author KHighness
 * @since 2021-04-30
 */

public class FinalDemo {
    int a = 3;
    static int A = 33333;
    final int b = 3;
    final static int B = 33333;

    public static void main(String[] args) {
        System.out.println(new FinalDemo().a);
        System.out.println(A);
        System.out.println(new FinalDemo().b);
        System.out.println(B);
    }
}
