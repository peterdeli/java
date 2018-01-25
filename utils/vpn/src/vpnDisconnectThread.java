
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;


public class vpnDisconnectThread extends Thread {
    
    public int connect(){
        return 0;
    }
    
    public vpnDisconnectThread(){
        setInterpreterPath();
        lines = new Vector();
        lineBuf = new StringBuffer();
        suspendFlag = false;
        start();
    }
    public vpnDisconnectThread(vpn3k gui){
        
        ui = gui;
        ui.stopUpdates();
        ui.startProgressBar();
        try{
            Thread.sleep(5000);
        } catch ( InterruptedException ix ){
        }
        setInterpreterPath();
        lines = new Vector();
        lineBuf = new StringBuffer();
        suspendFlag = false;
        ui.updateStatus("Disconnecting ..");
        
        //new runConnectionScript( ConnectScriptNames.PreDisconnectArg );
        start();
    }
    
    public void setInterpreterPath(){
        try{
            String expect_path = System.getProperty("EXPECT_PATH");
            if ( expect_path.length() < 1 || expect_path ==  null ){
                System.setProperty( "EXPECT_PATH", getInterpreter() );
            }
        } catch ( java.lang.NullPointerException np ){
            System.setProperty( "EXPECT_PATH", getInterpreter() );
        }
        System.out.println( "Expect interpreter: " + System.getProperty( "EXPECT_PATH" ));
    }
    
    public String getInterpreter(){
        String interpreter = null;
        String expect_path = null;
        if (System.getProperty("os.name").equals("SunOS")){
            interpreter = "/opt/sfw/bin/expect";
            
        } else if ( System.getProperty("os.name").equals( "linux" )){
            interpreter = "/usr/bin/expect";
        }
        try{
            expect_path = System.getProperty("EXPECT_PATH");
            if ( expect_path != null ){
                interpreter = expect_path;
            }
        } catch ( java.lang.NullPointerException np ){
            System.setProperty("EXPECT_PATH", interpreter );
        }
        return interpreter;
    }
    public void Disconnect( String acct ){
        profile = acct;
        this.TerminateVpnClient();
    }
    
    public void Disconnect( ){
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
        
        try {
            // Execute command
            child = Runtime.getRuntime().exec(cmd_args);
            inputStream = new childDisconnectThread( this, child.getInputStream(), "in" );
            errorStream = new childDisconnectThread( this, child.getErrorStream(), "err" );
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main( String[] args ){
        vpnDisconnectThread terminator = new vpnDisconnectThread();
    }
    public void run() {
        this.Disconnect();
    }
    
    
    protected String pathDelim = System.getProperty( "file.separator" );
    protected final String command = System.getProperty("user.dir" ) + pathDelim + "vpnDisconnect.exp";
    //protected static final String interpreter = System.getProperty( "EXPECT_PATH" );
    protected final String interpreter = getInterpreter();
    protected static final String interp_opts = "-f";
    protected String profile;
    protected Vector lines = new Vector();
    protected StringBuffer lineBuf = new StringBuffer();
    protected vpn3k ui;
    protected boolean suspendFlag;
    protected Thread disconnectThread;
    protected childDisconnectThread inputStream;
    protected childDisconnectThread errorStream;
    protected Process child;
    
}

class childDisconnectThread extends Thread {
    
    private InputStream in;
    private OutputStream err;
    private vpnDisconnectThread vpn;
    
    public childDisconnectThread( vpnDisconnectThread t, InputStream stream, String type) {
        vpn = t;
        if ( type.equals( "in" ))
            in = stream;
       
        start();
        
    }
     public childDisconnectThread( vpnDisconnectThread t, OutputStream stream, String type) {
        vpn = t;
        if ( type.equals( "err" ))
            err = stream;
        start();
        
    }
    
    public void closeError() {
        System.out.println( "Close error stream" );
        try{
            err.close();
        } catch ( java.io.IOException x ){
        }
    }
    
    public void closeInput() {
        System.out.println( "Close input stream" );
        try{
            in.close();
        } catch ( java.io.IOException x ){
        }
    }
    
    public void run() {
        int c ;
        StringBuffer sb = new StringBuffer();
        try{
            if ( in instanceof InputStream ){
                System.out.println( "\nInput Stream (vpnDisconnectThread)" );
                while ( ( c=in.read() ) != -1 ){
                    
                    if ( (char)c == '\n' ){
                        System.out.flush();
                        this.vpn.ui.updateLog( sb.toString() );
                        sb = new StringBuffer();
                    }
                    sb.append((char)c);
                    if ( sb.indexOf("Your VPN connection has been terminated.") >= 0 ||
                         sb.indexOf("connection is not active") >= 0 ) {
                        if ( this.vpn.ui instanceof vpn3k ){
                            String status = new String( "Disconnected" );
                            this.vpn.ui.stopProgressBar();
                            this.vpn.ui.updateStatus(status);
                            this.vpn.ui.Connected = false;
                            this.vpn.ui.Connecting = false;
                            this.vpn.ui.enableConnectButton();
                            this.vpn.ui.reloadConfigs();
                            //new runConnectionScript( ConnectScriptNames.PostDisconnectArg );
                        }
                        sb = new StringBuffer();
                        
                        this.vpn.child.waitFor();
                       // this.vpn.ui.startUpdates();
                        System.out.println( "Child process finished (vpnDisconnectThread)" );
                    }
                    System.out.print( (char)c );
                }
                
                return;
                
            } else if ( err instanceof OutputStream ){
                System.out.println( "\nError Stream (vpnDisconnectThread)"  );
                err.close();
                return;
            }
        } catch ( Exception x ){}
    }
    
}
