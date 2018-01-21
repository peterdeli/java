import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
class Main {
    static int ceiling = 20;
    static int arrayLen = 5;
    public static int getRandInt(int max){
        return (int) Math.floor(Math.random() * max);
    }
    public static void main(String[] args) {
        ArrayList<Integer> aList = new ArrayList<>(Main.arrayLen);
        for ( int i=0; i<arrayLen; i++){
            aList.add( getRandInt(Main.ceiling) );
        }
        System.out.println( "array populated " + aList.size() + " items.");
        Integer findNum = aList.get( getRandInt( ( Main.arrayLen - 1)));
        System.out.println( "Searching for match for " + findNum );
        System.out.println( "Find " + findNum + " in list.");
        Iterator<Integer> iter = aList.iterator();
        while ( iter.hasNext() ){
            Integer item = Integer.valueOf(iter.next());
            if ( item == findNum ) {
                System.out.println("Item: Matched " + item + " at index " + aList.indexOf(item));
            } else {
                System.out.println( "Item: " + item );
            }
        }
    }
}
