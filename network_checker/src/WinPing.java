import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.regex.Pattern;

public class WinPing {
     private static final String pingCommand = "ping.exe";
     
     
     protected boolean isIPAddress( String addr ){
         return Pattern.matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+", addr );
     }
     protected boolean isHostname ( String host ){
         try {
             InetAddress.getByName(host);
         } catch ( java.net.UnknownHostException uh ){
             return false;
         }
         return true;
     }
     public String getIPAddress(String host){
        try {
             return InetAddress.getByName(host).toString();
         } catch ( java.net.UnknownHostException uh ){
             return null;
         }
     }
     
     public boolean isValidHost(String host){
        
         try {
             InetAddress.getByName(host);
         } catch ( java.net.UnknownHostException uh ){
             return false;
         }
         
         return true;
     }
     
     public static boolean isAlive (String host )
     {
         try {
             String[] pingCmd = { pingCommand, "-n", "1", host };
             java.lang.Process process = Runtime.getRuntime ().exec ( pingCmd );
             return process.waitFor () == 0;
         } catch (IOException ioe) {
             
         } catch (InterruptedException ie) {
            
         }
         return false;
     }
 
     public static void main (String[] arg) throws Exception
     {
        String host = JOptionPane.showInputDialog( new JFrame(), "Ping", arg[0] );
        
         boolean val = isAlive ( host  );
         if (val)
             System.out.println ( host + " is alive.");
         else
             System.out.println ( "No response from " + host );
         
         System.exit(0);
     }
 }