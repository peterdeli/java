
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;


public class vpnDisconnect extends Thread {
    
    public int connect(){
        return 0;
    }
    
    public vpnDisconnect(){
        
    }
    public vpnDisconnect( vpn3k vpnUI){
        //command = "/home/pdel/VPN3K/vpnDisconnect.exp";
        
        // Run pre-disconnect scripts
        new runConnectionScript( ConnectScriptNames.PreDisconnectArg );
        try {
            Thread.currentThread().sleep( 1000 );
        } catch ( java.lang.InterruptedException x  ){
            x.printStackTrace();
        }
        
        UI = vpnUI;
        lines = new Vector();
        lineBuf = new StringBuffer();
        
        
        suspendFlag = false;
        disconnectThread = new Thread( this, "vpnclient thread" );
        disconnectThread.start();
    }
    
    public void Disonnect( String acct ){
        profile = acct;
        this.TerminateVpnClient();
    }
    
    public void Disconnect( ){
        // UserInfo userinfo = new UserInfo(this);
        // UserInfo pops up dialog with user/token fields
        // sets user/token
        //new AuthDialog(UI);
        this.TerminateVpnClient();
    }
    
    
    private void TerminateVpnClient( ){
        
        
        String[] cmd_args = { interpreter, interp_opts, command };
        if ( new File( command ).exists() != true ) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No such file " + command );
            System.exit(0);
        }
        
        boolean newLine = false;
        String lastWord = null;
        //if ( UI.clientConnection instanceof vpnConnect ){
        //    UI.clientConnection.abort();
        //}
        this.UI.updateStatus("Disconnecting ...");
        //this.UI.stopConnection();
        //this.UI.updateStatus("Disconnected");
        
        try {
            // Execute command
            
            Process child = Runtime.getRuntime().exec(cmd_args);
            
            //child.waitFor();
            
            //Thread.sleep(2000);
            // Get input stream to read from it
            InputStream in = child.getInputStream();
            OutputStream out = child.getOutputStream();
            
            //String response =  JOptionPane.showInputDialog( new javax.swing.JFrame(), "Enter Password: " );
            //String password = response.concat("\n");
            int c;
            int count = 0;
            while ((c = in.read()) != -1) {
                Thread.yield();
                if ( (char)c == '\n' ){
                    
                    if ( lineBuf.indexOf("terminated") >= 0  ) {
                        //UI.setConnectionStatus(false);
                        // UI.stopUpdates();
                        //UI.Connected = false;
                        //UI.setConnectButton();
                        //UI.enableConnectButton();
                        this.UI.updateStatus("Disconnected");
                        this.UI.updateLog(lineBuf.toString());
                        this.UI.Connected = false;
                        this.UI.Connecting = false;
                        
                        //UI.startUpdates();
                        // Run post-disconnect scripts
                    }
                    // start new line
                    Thread.sleep(2);
                    lineBuf.append((char)c);
                    lines.add(lineBuf);
                    if ( UI instanceof vpn3k ){
                        String updateString = new String( "vpnDisconnect: " + lineBuf.toString() );
                        UI.updateLog(updateString);
                    }
                    
                    lineBuf = new StringBuffer();
                } else {
                    lineBuf.append((char)c);
                }
                System.out.print((char)c);
                
            }
            //UI.setConnectionStatus(false);
            //UI.Connected = false;
            //UI.Connecting = false;
            UI.enableConnectButton();
            this.UI.updateStatus("Disconnected");
            
            // reload accounts
            //this.UI.loadConfigs();
            //this.UI.loadInitFile();
            //this.UI.initColumns();
            this.UI.reloadConfigs();
            new runConnectionScript( ConnectScriptNames.PostDisconnectArg );
            //out.flush();
            //out.close();
            //in.close();
            child.waitFor();
            System.out.println( "Child process finished (vpnDisconnect.java)" );
            //child.destroy();
            //System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    
    public static void main( String[] args ){
        vpnDisconnect terminator = new vpnDisconnect();
        
    }
    
    
    public void run() {
        //this.execVpnClient();
        
        //new AuthDialog(UI);
        try{
            synchronized(this){
                while (suspendFlag){
                    wait();
                }
            }
        } catch ( Exception ex ){
            ex.printStackTrace();
        }
        this.Disconnect();
    }
    private String pathDelim = System.getProperty( "file.separator" );
    private final String command = System.getProperty("user.dir" ) + pathDelim + "vpnDisconnect.exp";
    //private static  String command = "/home/pdel/VPN3K/vpnDisconnect.exp";
    //private static final String interpreter = "/usr/bin/expect";
    private static final String interpreter = System.getProperty( "EXPECT_PATH" );
    private static final String interp_opts = "-f";
    private String profile;
    
    
    private Vector lines = new Vector();
    private StringBuffer lineBuf = new StringBuffer();
    
    
    public vpn3k UI;
    boolean suspendFlag;
    Thread disconnectThread;
}
