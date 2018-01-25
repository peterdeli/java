/*
 * sendInput.java
 *
 * Created on October 20, 2003, 5:22 PM
 */

/**
 *
 * @author  pdel
 */
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;


public class sendInput_1 {
    
    /** Creates a new instance of sendInput */
    public sendInput_1() {
        
    }
    
    public static void main( String[] args ){
        boolean reset = false;
        try {
            // Execute command
            String[] command = { "/usr/local/bin/vpnclient", "connect", "ebay" };
            Process child = Runtime.getRuntime().exec(command);
            
            // Get input stream to read from it
            InputStream in = child.getInputStream();
            OutputStream out = child.getOutputStream();
            int c;
            while ((c = in.read()) != -1) {
               
                   
                   System.out.print ( (char)c );
               
            }
            
            
        } catch ( java.io.IOException ioe ){
            ioe.printStackTrace();
        }
        
        //Thread.sleep(10000);
        

    }
    
}
