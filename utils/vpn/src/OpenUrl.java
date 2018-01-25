/*
 * SystemExec.java
 *
 * Created on February 3, 2004, 12:55 PM
 */

/**
 *
 * @author  pdel
 */

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

public class OpenUrl extends Thread {
    
    /** Creates a new instance of SystemExec */
    public OpenUrl(vpn3k ui, String url) {
        
        URL = url;
        UI=ui;
        Browser = new String( BrowserPath + PathDelim + openmoz );
        start();
    }
    
    public OpenUrl(String url) {
       
        URL = url;
        Browser = new String( BrowserPath + PathDelim + openmoz );
        start();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //OpenUrl u = new OpenUrl(  "/opt/ITvpnclient/docs/SunStart.html" );
        OpenUrl u = new OpenUrl(  "sunweb.central.sun.com" );
       //usr/lib/mozilla-1.0.1/mozilla-bin
    }
    
    public void run() {
        
        try {
            // Execute command
            String[] CommandArgs = new String[4];
            CommandArgs[0] = "/bin/bash";
            CommandArgs[1] = this.Browser;
            CommandArgs[2] = "--attach";
            CommandArgs[3] = this.URL;
            System.out.println( "Starting " + CommandArgs[1] + " URL: " + CommandArgs[3] );
            Process child = Runtime.getRuntime().exec(CommandArgs);
            
            
            // Get input stream to read from it
            /*
            InputStream in = child.getInputStream();
            OutputStream out = child.getOutputStream();
            
            
            InputStreamReader is = new InputStreamReader( in );
            BufferedReader br = new BufferedReader( is );
            
            while ( true ) {
               
                String line = br.readLine();
                if ( line.length() > 0 ){
                System.out.println( line );
                } else {
                    System.out.println("no output");
                    sleep(2000);
                }
                
            }
             **/
            //child.waitFor();
            
        } catch ( java.lang.Exception x ) {
            
        }
    }
    
    private String openmoz = "openmoz";
    private String Browser = "";
    private String BrowserPath = System.getProperty("user.dir");
    private String PathDelim = System.getProperty( "file.separator" );
    private String URL = "";
    private vpn3k UI;
}
