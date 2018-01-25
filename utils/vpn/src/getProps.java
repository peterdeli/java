/*
 * getProps.java
 *
 * Created on November 13, 2003, 8:01 PM
 */

/**
 *
 * @author  pdel
 */
public class getProps {
    
    /** Creates a new instance of getProps */
    public getProps() {
        System.out.println( "property path: " + System.getProperty("vpn3k") );
    }
    
      public getProps(String path) {
        System.out.println( "property path: " + path );
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        getProps p = new getProps(args[0]);
    }
    
}
