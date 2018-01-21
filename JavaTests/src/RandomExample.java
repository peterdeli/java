import java.util.Random;

class RandomExample
{
    public static String doThis (String text)
    {
        return ( "A: " + text + " B: " + Integer.toString( Math.abs( new Random().nextInt()) ));
    }
    public static void main (String args[]) throws InterruptedException
    {
       while( true ) {
           System.out.println( doThis(  Integer.toString( Math.abs( new Random().nextInt() ) )));
           Thread.currentThread().sleep(1000);
       }
    }
}
