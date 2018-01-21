import java.util.Random;
import static java.lang.System.*;
class StaticMain {
    final static short x = 2;
    public static int y = 0;

    public static void main(String[] args) {
        out.println( runMain.hello );
        for (int z = 0; z < 3; z++) {
            switch (z) {
                case x:
                    System.out.print("0 ");
                    break;
                case x - 1:
                    System.out.print("1 ");
                    break;
                case x - 2:
                    System.out.print("2 ");
                    break;
            }
        }
    }
}

class runMain{
    // default public protected private
    static String hello = "hello";
    static int numberOne = 1;

}
