/*
 * runIt.java
 *
 * Created on October 21, 2003, 2:56 PM
 */

/**
 *
 * @author  pdel
 */

 import java.io.*;
 public class CmdExec {
   public CmdExec() {
      //String[] cmdline = { "/usr/local/bin/vpnclient", "connect", "ebay" };
       //String[] cmdline = { "/usr/local/bin/vpnclient" };
       String[] cmdline = { "/usr/bin/expect", "-f", "/home/pdel/vpn3k/vpn.expect" };


       try {
           String line;
           Process p = Runtime.getRuntime().exec(cmdline);
           BufferedReader input =
           new BufferedReader
           (new InputStreamReader(p.getInputStream()));
           
           
           while (  ( line=input.readLine()) != null ){
               
               System.out.println( "line is " + line.length() + " long" );
               System.out.println( line );
           }
           
           input.close();
       }
       catch (Exception err) {
           err.printStackTrace();
       }
   }

 public static void main(String argv[]) {
   new CmdExec();
   }
}
