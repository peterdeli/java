
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class runConnectionScript {
    
    
    public runConnectionScript(){
        
    }
    
    public runConnectionScript(String arg){
        scriptArg = arg;
        runScript();
    }
    
    public void runScript(){
        
        try {
        String[] cmd_args = { interpreter, interp_opts, command, scriptArg };
        childProcess = Runtime.getRuntime().exec(cmd_args);
        
        // Get input stream to read from it
        InputStream in = childProcess.getInputStream();
        OutputStream out = childProcess.getOutputStream();
        //String response =  JOptionPane.showInputDialog( new javax.swing.JFrame(), "Enter Password: " );
        //String password = response.concat("\n");
        int c;
        int count = 0;
        // set an alarm if no response within one 30 sec.
        
        while ((c = in.read()) != -1) {
            // reset counter each time
            // start counter
            Thread.currentThread().yield();
            System.out.print((char)c);
        }
        } catch ( java.io.IOException io ){
            io.printStackTrace();
            
        }
    }
    
    public static void main( String[] args ){
        new runConnectionScript( "pre_connect" );
    }
    
    //private static  String command = "vpnConnect.exp";
    //private static final String interpreter = "/usr/bin/expect";
    private static final String interpreter = System.getProperty( "EXPECT_PATH" ); 
    private static final String interp_opts = "-f";
    private String pathDelim = System.getProperty( "file.separator" );
    private final String command = System.getProperty("user.dir" ) + pathDelim + "do_connection_script.tcl";
    private Process childProcess;
    private String scriptArg;
}
    
