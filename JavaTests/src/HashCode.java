class HashCode
{

    public static void main (String args[])
    {
        int x = 0;
        Integer x1 = new Integer( 1);
        Integer x2 = new Integer( 2);
        Integer x3 = new Integer( 3);
        Integer x4 = new Integer( 3);
        Integer x5 = new Integer( 5);
        Integer x6 = new Integer( 6);
        Integer x7 = new Integer( 7);
        Integer x8 = new Integer( 7);

        if (x1.hashCode() != x2.hashCode() )  x = x + 1;
        if (x3.equals(x4) )  x = x + 10;
        if (!x5.equals(x6) ) x = x + 100;
        if (x7.hashCode() == x8.hashCode() )  x = x + 1000;
        System.out.println("x = " + x);

    }
}
