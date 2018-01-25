import java.io.*;
import javax.swing.*;
import java.net.*;
import java.util.regex.Pattern;
import java.util.Timer;
import java.util.TimerTask;

public class LinuxPing {
    private static final String pingCommand = "/bin/ping";
    protected Process process;
    protected Timer timer;
    private long timeout = 10000;
    private String hostname;
    
    protected boolean isIPAddress( String addr ){
        return Pattern.matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+", addr );
    }
    protected boolean isHostname( String host ){
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
    class ProcessHandler extends TimerTask{
        
        public void run() {
            System.out.println( "Timeout waiting for " + hostname + " !" );
            process.destroy();
        }
        
    }
    public boolean isAlive(String host ) {
        hostname = host;
        try {
            String[] pingCmd = { pingCommand, "-c", "1", host };
            this.process = Runtime.getRuntime().exec( pingCmd );
            // start a timer
            this.timer = new Timer();
            
            timer.schedule( new ProcessHandler(), timeout);
            if ( process.waitFor() == 0 ){
                timer.cancel();
                return true;
            } else {
                timer.cancel();
                return false;
            }
            
        } catch (IOException ioe) {
            System.out.println( "IO Exception!" );
        } catch (InterruptedException ie) {
            System.out.println( "Process interrupted or timed out!" );
        }
        timer.cancel();
        return false;
    }
    
    public static void main(String[] arg) throws Exception {
        String host = "";
        if ( arg.length == 1){
            host = JOptionPane.showInputDialog( new JFrame(), "Ping", arg[0] );
        } else {
            
            host = JOptionPane.showInputDialog( new JFrame(), "Ping", "Enter Host" );
        }
        LinuxPing lp = new LinuxPing();
        boolean val = lp.isAlive( host  );
        if (val){
            System.out.println( host + " is alive.");
            JOptionPane.showMessageDialog(new JFrame(), new String( host + " is alive." ));
        } else {
            System.out.println( "No response from " + host );
            JOptionPane.showMessageDialog(new JFrame(), new String("No response from " + host  ));
        }
        System.exit(0);
    }
}