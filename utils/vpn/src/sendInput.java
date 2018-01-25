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


public class sendInput {
    
    /** Creates a new instance of sendInput */
    public sendInput() {
        
    }
    
    public static void main( String[] args ){
        boolean reset = false;
        try {
            // Execute command
            String command = "/tmp/dig";
            Process child = Runtime.getRuntime().exec(command);
            
            // Get input stream to read from it
            InputStream in = child.getInputStream();
            OutputStream out = child.getOutputStream();
            int c;
            while ((c = in.read()) != -1) {
               if ( (char)c == '>' ){
                   System.out.print( (char)c );
                   in.mark(10);
                   out.write( "Yahoo\n".getBytes() );
                   out.flush();
                   System.out.print( "Yahoo\n" );
                   //System.out.println( "Yahoo" );
                   
               } else {
                   
                   System.out.print( (char)c );
               }
            }
            
            
        } catch ( java.io.IOException ioe ){
            ioe.printStackTrace();
        }
        
        //Thread.sleep(10000);
        

    }
    
}
