
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class vpnConnect extends Thread {
    
      
    public int connect(){
    return 0;   
    }
    
    public vpnConnect(){
        
    }
    
    public vpnConnect(String acctProfile){
         profile = acctProfile;
    }
    
    public vpnConnect(vpn3k vpnUI ){
         //command = "/home/pdel/VPN3K/vpnConnect.exp";
         //command = "vpnConnect.exp";
         //command = new File(".").getAbsolutePath() + "/" +  "vpnConnect.exp";
         System.out.println( "Current Dir: " + System.getProperty( "user.dir" ));
         this.UI = vpnUI;
         profile = this.UI.getAccountSelected();
         
         lines = new Vector();
         lineBuf = new StringBuffer();
        //String defaultUser = this.readDefaultUser();
         defaultUser = this.UI.getSelectedAcctAttribute("Username");
         suspendFlag = false;
         connectThread = new Thread( this, "vpnclient thread" );
         this.connecting = true;
         
        connectThread.start();
    }
    
    public vpnConnect(String acctProfile, vpn3k vpnUI ){
         //command = "/home/pdel/vpn3k/vpnConnect.exp";
        System.out.println( "Current Dir: " + System.getProperty( "user.dir" ));
         //command = "/home/pdel/VPN3K/vpnConnect.exp";
         //command = System.getProperty("user.dir" ) + pathDelim + "vpnConnect.exp";
         
         //command = new File(".").getAbsolutePath() + "/" +  "vpnConnect.exp";
         System.out.println( "command: " + command );
         profile = acctProfile;
         UI = vpnUI;
         lines = new Vector();
         lineBuf = new StringBuffer();
         new AuthDialog(this.UI);
         
         if ( this.UI.getUserID() == null || this.UI.getTokenPass() == null || this.UI.ConnectionCancelled == true ){
             if ( connectionTimer instanceof Timer ){
                    connectionTimer.cancel();
             }
              return;
         } else {
             
             // Run pre-connection scripts
             new runConnectionScript( ConnectScriptNames.PreConnectArg );
             
             
             //String defaultUser = this.readDefaultUser();
             //defaultUser = this.UI.getSelectedAcctAttribute("Username");
             defaultUser = this.UI.getUserID();
             suspendFlag = false;
             connectThread = new Thread( this, "vpnclient thread" );
             this.connecting = true;
             this.UI.updateStatus("Connecting ..");
             
             this.UI.updateLog("Connecting as " + defaultUser );
             connectThread.start();
             
             try{
                 connectionTimer = new Timer();
                 connectionTimer.schedule( new checkConnection("connecting", connectionTimer, this ), (long)30000 );
                 
                 connectionTimer = new Timer();
                 connectionTimer.schedule( new checkConnection("authenticating", connectionTimer, this ), (long)40000 );
                 
                 connectionTimer = new Timer();
                 connectionTimer.schedule( new checkConnection("connected", connectionTimer, this ), (long)60000 );
                 
             } catch ( Exception ie ){
                 this.UI.updateLog( "Error from Connection Timer: " );
                 this.UI.updateLog(  ie.getLocalizedMessage() );
             }
         }
    }
    public void abort(){
        try{
            this.childProcess.destroy();
            
        }catch ( Exception x ){
            
        }
    }
    public void Connect( String acct ){
        profile = acct;
        this.execVpnClient();
    }
    
    public void Connect( ){
        // UserInfo userinfo = new UserInfo(this);
        // UserInfo pops up dialog with user/token fields
        // sets user/token 
        //new AuthDialog(UI);
        
        
        
        this.execVpnClient();
        System.out.println( "End of Connect (vpnConnect)" );
    }
  
    
    private void execVpnClient( ){
        
  
        String[] cmd_args = { interpreter, interp_opts, command, profile };
        if ( new File( command ).exists() != true ) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No such file " + command );
            return;
        }
        
        boolean newLine = false;
        String lastWord = null;
        
        
        try {
            // Execute command
            
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
                Thread.yield();
                
                
                if ( (char)c == '\n' ){
                    // start new line
                    Thread.sleep(2);
                    lineBuf.append((char)c);
                    lines.add(lineBuf);
                    if ( this.UI instanceof vpn3k ){
                        this.UI.updateLog(lineBuf.toString());
                    }
                 
                    if ( lineBuf.indexOf("Your VPN connection is secure") >= 0 ){
                        this.connected = true;
                        connectionTimer.cancel();
                        this.UI.setConnectionStatus(true);
                        Hashtable acct = this.UI.getAccountInfo(profile);
                        String concentrator = (String)acct.get(ProfileFields.Host);
                        this.UI.updateStatus(new String( "Connected to " + concentrator ));
                        this.UI.setConnectButton();
                        this.UI.invalidate();
                        this.UI.validate();
                        
                        // Run post-connection scripts
                        new runConnectionScript( ConnectScriptNames.PostConnectArg );
                        
                    } else if ( lineBuf.indexOf( "A connection already exists" ) >= 0 ){
                        if ( JOptionPane.showConfirmDialog(new javax.swing.JFrame(), "A vpn connection already exists. Press OK to disconnect it.", "Error", JOptionPane.OK_CANCEL_OPTION ) == 0 ){
                            new vpnDisconnect( this.UI );
                            this.connectionTimer.cancel();
                        } else {
                            // set up as if alredy connected
                            this.connectionTimer.cancel();
                            this.connected = true;
                            this.UI.setConnectionStatus(true);
                            this.UI.updateStatus();
                            this.UI.setConnectButton();
                            this.UI.invalidate();
                            this.UI.validate();
                        }
                        break;
                        
                    } else if ( lineBuf.indexOf("Your VPN connection has been terminated") >= 0 ) {
                        
                        // Run post-connection scripts
                        new runConnectionScript( ConnectScriptNames.PostDisconnectArg );
                        this.connected = false;
                        this.UI.setConnectionStatus(false);
                        this.UI.updateStatus();
                        this.UI.setConnectButton();
                        this.UI.invalidate();
                        this.UI.validate();
                        this.connectionTimer.cancel();
                        
                        // close down this thread
                        
                    } else if ( lineBuf.indexOf("authentication failed") >= 0 ){
                       
                        // Run post-connection scripts
                        new runConnectionScript( ConnectScriptNames.PostDisconnectArg );
                        this.UI.setConnectionStatus(false);
                        this.UI.updateStatus();
                        this.UI.setConnectButton();
                        this.UI.invalidate();
                        this.UI.validate();
                        this.connectionTimer.cancel();
                    }
                    lineBuf = new StringBuffer();
                } else {
                    lineBuf.append((char)c);
                }
                System.out.print((char)c);
                
                
                if (  lineBuf.indexOf("USERNAME:")  >= 0  ){
                    
                    this.authenticating = true;
                    if ( UsernamePromptSeen == 0 ){
                        // if first time, we already
                        // popped up AuthDialog when
                        // we created vpnConnect instance
                        
                        UsernamePromptSeen++;
                    } else {
                        new AuthDialog(this.UI);
                    }
                    if ( UI.getUserID() == null || UI.getTokenPass() == null ){
                        break;
                    }
                    String username = this.UI.getUserID().concat( "\n" );
                    
                    out.write( username.getBytes() );
                    out.flush();
                    Thread.sleep( 2000 );
                    lines.add(lineBuf);
                    lineBuf = new StringBuffer();
                    //System.out.print( "pd33162@vpn\n" );
                    
                } else if ( lineBuf.indexOf("PASSWORD:") >= 0 ){
                    this.authenticating = true;
                    String password = this.UI.getTokenPass().concat( "\n" );
                    out.write( password.getBytes() );
                    out.flush();
                    Thread.sleep( 2000 );
                    lines.add(lineBuf);
                    lineBuf = new StringBuffer();
                    //System.out.print( password );
                    
                    
                } else if ( lineBuf.indexOf("connection has been terminated") >= 0  ) {
                    
                    this.connected = false;
                    if ( this.UI instanceof vpn3k ){
                        // Run post-connection scripts
                        new runConnectionScript( ConnectScriptNames.PostDisconnectArg );
                        this.connected = false;
                        this.UI.setConnectionStatus(false);
                        this.UI.updateStatus("No Connection");
                        lineBuf.append('\n');
                        this.UI.updateLog(lineBuf.toString());
                        this.UI.setConnectButton();
                        this.UI.invalidate();
                        this.UI.validate();
                        this.connectionTimer.cancel();
                     }
                    break;
                } else if ( lineBuf.indexOf("EOF from vpnclient") >= 0 ) {
                    
                     this.connected = false;
                     this.connectionTimer.cancel();
                    if ( this.UI instanceof vpn3k ){
                        // Run post-connection scripts
                        new runConnectionScript( ConnectScriptNames.PostDisconnectArg );
                        this.connected = false;
                        this.UI.setConnectionStatus(false);
                        this.UI.updateStatus("No Connection");
                        lineBuf.append('\n');
                        this.UI.updateLog(lineBuf.toString());
                        this.UI.setConnectButton();
                        this.UI.invalidate();
                        this.UI.validate();
                     }
                    break;
                }
            }
            childProcess.destroy();
            in.close();
            //System.exit(0);
        } catch (Exception e) {
            System.out.println( "Error from execVpnClient: " + e.getMessage() );
            e.printStackTrace();
        }
       System.out.println( "End of execVpnClient");
    }
    
    private String readDefaultUser (File path) {
       String defaultUser = ""; 
       
       return defaultUser;
    }
    
    public boolean isAuthenticating(){
        return this.authenticating;
    }
    
    public boolean isConnected(){
        System.out.println( "Connection established is: " + this.UI.Connected );
        return this.UI.Connected;
    }
    public boolean isConnecting(){
        return this.connecting;
    }
    
    public boolean connectionCancelled(){
        
        return this.UI.ConnectionCancelled;
    }
    private String readDefaultUser(String path) {
        String defaultUser = "";
        
        return defaultUser;
    }
    
    private String readDefaultUser() {
        String defaultUser = "";
        
        return defaultUser;
    }
    public static void main( String[] args ){
        vpnConnect connection = new vpnConnect( args[0] );
        
    }
    
    void mySuspend(){
        suspendFlag = true;
    }
    synchronized void myResume(){
        suspendFlag = false;
        notify();
    }
    public void run() {
      try{
          this.Connect();
        } catch ( Exception ex ){
            ex.printStackTrace();
        }
        
       System.out.println( "End of vpnConnect.java (run)");
    }
    
    //private static  String command = "vpnConnect.exp";
    private static final String interpreter = "/usr/bin/expect";
    private static final String interp_opts = "-f";
    private String profile;
    
    
    private Vector lines = new Vector();
    private StringBuffer lineBuf = new StringBuffer();
    //String defaultUser = this.readDefaultUser();
    private String defaultUser = "[First Initial][Last Initial][EmployeeNumber]@vpn";
    
    protected vpn3k UI;
    private String account;
    private String userName;
    private String passwd;
    boolean suspendFlag;
    Thread connectThread;
    private int	UsernamePromptSeen = 0;
    private String pathDelim = System.getProperty( "file.separator" );
    private final String command = System.getProperty("user.dir" ) + pathDelim + "vpnConnect.exp";
    private boolean connecting = false;
    private boolean connected = false; // connection complete 
    private boolean authenticating = false; // connection authenticating
    Process childProcess;
    protected Timer connectionTimer;
}

class timerThread implements Runnable {
            String name;
            Thread t;
            int seconds;
            
            timerThread( String threadName, int delay ){
                name = threadName;
                t=new Thread( this, name);
                seconds = delay;
                t.start();
                
            }
            public void run(){
                try{
                    Thread.sleep( ( seconds * 1000 ));
                    Thread.yield();
                }catch ( InterruptedException e ){
                    
                }
            }
 }

 class checkConnection extends TimerTask{
            String type;
            Timer timer;
            vpnConnect process;
            checkConnection( String check, Timer t, vpnConnect proc ){
                type = check;
                timer = t;
                process = proc;
            }
            public void run(){
                if ( process.currentThread().isAlive() ){
                    if ( process.connectionCancelled() == true ) return;
                    if ( type.compareTo("connecting") == 0 ){
                        if ( process.isConnected() ) return;
                        process.UI.updateLog( "Verifying Connection\n" );
                        if ( process.isConnecting() == false ){
                            System.out.println( "Connection hanging" );
                            process.UI.updateLog("Connection hanging ..");
                        } else {
                            process.UI.updateLog("Connection Initiated\n");
                        }
                    } else if ( type.compareTo("authenticating") == 0 ){
                        if ( process.connectionCancelled() == true ) return;
                        if ( process.isConnected() ) return;
                        if ( process.isAuthenticating() == false ){
                            System.out.println( "Connection not authenticated" );
                            process.UI.updateLog("Connection not authenticated .." );
                        } else {
                            process.UI.updateLog("Connection Authenticated\n" );
                        }
                    } else if (type.compareTo("connected") == 0 ){
                        if ( process.connectionCancelled() == true ) return;
                        if ( process.isConnected() == false ){
                            System.out.println( "No Connection, aborting" );
                            process.UI.updateLog("No Connection, aborting");
                            process.abort();
                            process.UI.setConnectionStatus(false);
                            process.UI.setConnectButton();
                        } else {
                            process.UI.updateLog("Connection Established\n");
                        }
                    }
                }
                timer.cancel();
            }
        }