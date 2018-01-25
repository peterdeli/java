
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;


public class VpnClient_1 {
    
    
    public static void main( String[] args ){
        //String command = "/usr/local/bin/vpnclient";
        //String command = "/home/pdel/bin/vpn.sh";
        //String [] cmd_args = { command, "connect", "ebay" };
        //String [] cmd_args = { "/usr/bin/expect", "-f", command };
        
        //String [] cmd_args = { "/bin/sh", "-c ", command  };
        //String [] cmd_args = { "/bin/sh", "-c ", command  };
        
        //This works!
        String command = "/home/pdel/bin/vpn.sh";
        String [] cmd_args = { "/bin/sh", "-c", command };
        
        
        Vector lines = new Vector();
        StringBuffer lineBuf = new StringBuffer();
        
        if ( new File( command ).exists() != true ) {
            JOptionPane.showMessageDialog(new JFrame(), "No such file " + command );
            System.exit(0);
        }
        boolean newLine = false;
        String lastWord = null;
        
            
        try {
        // Execute command
       
        Process child = Runtime.getRuntime().exec(cmd_args);
    
        // Get input stream to read from it
        InputStream in = child.getInputStream();
        OutputStream out = child.getOutputStream();
        //String response =  JOptionPane.showInputDialog( new JFrame(), "Enter Password: " );
        //String password = response.concat("\n");
        int c;
        int count = 0;
        while ((c = in.read()) != -1) {
            
            
            if ( (char)c == '\n' ){
                // start new line
                lines.add(lineBuf);
                lineBuf = new StringBuffer();
            } else {
                lineBuf.append((char)c);
            }
            System.out.print((char)c);
            
            if ( (Pattern.matches("Username *: ",  lineBuf )) == true  ){
            
                        
                // Username prompt
                out.write( "pd33162@vpn\n".getBytes() );
                out.flush();
                Thread.sleep( 2000 );
                lines.add(lineBuf);
                lineBuf = new StringBuffer();
                continue;
                
            } else if ( (Pattern.matches("Password *: ",  lineBuf )) == true ){
                // Username prompt
                
                //String response = "abcdefg\n";
                String response =  JOptionPane.showInputDialog( new JFrame(), "Enter Password: " );
                String password = response.concat("\n");
                out.write( password.getBytes() );
                out.flush();
                Thread.sleep( 2000 );
                lines.add(lineBuf);
                lineBuf = new StringBuffer();
                //System.out.print( password );
                
                
            } else if ( (Pattern.matches("*wish to continue*",  lineBuf )) == true ){
                out.write( "y\n".getBytes() );
                out.flush();
                Thread.sleep( 2000 );
                lines.add(lineBuf);
                lineBuf = new StringBuffer();
            }
        }
        in.close();
    } catch (Exception e) {
        e.printStackTrace();
    } 
        
    }
}
