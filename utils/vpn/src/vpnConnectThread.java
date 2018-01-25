
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class vpnConnectThread extends Thread {
    
    public vpnConnectThread(String acctProfile, String id, String pass ){
        profile = acctProfile;
        userName = id;
        passwd = pass;
        UsernamePromptSeen =  1;
        System.out.println( "Current Dir: " + System.getProperty( "user.dir" ));
        lines = new Vector();
        lineBuf = new StringBuffer();
        start();
        System.out.println( "End of constructor" );
    }
    
    public vpnConnectThread(String account){
        profile = account;
        String arglist = JOptionPane.showInputDialog(null,"Enter  id, pass");
        if ( arglist == null || arglist.length() < 1 ) return;
        StringTokenizer st = new StringTokenizer( arglist, " " );
        String[] argArray = new String[2];
        for ( int i=0; i<argArray.length; i++ ){
            argArray[i] = st.nextToken();
        }
        userName = argArray[0];
        passwd = argArray[1];
        UsernamePromptSeen =  1;
        System.out.println( "Current Dir: " + System.getProperty( "user.dir" ));
        lines = new Vector();
        lineBuf = new StringBuffer();
        start();
        System.out.println( "End of constructor" );
    }
    public vpnConnectThread(){
        String arglist = JOptionPane.showInputDialog(null,"Enter profile, id, pass");
        if ( arglist == null || arglist.length() < 1 ) return;
        StringTokenizer st = new StringTokenizer( arglist, " " );
        String[] argArray = new String[3];
        for ( int i=0; i<argArray.length; i++ ){
            argArray[i] = st.nextToken();
        }
        profile = argArray[0];
        userName = argArray[1];
        passwd = argArray[2];
        UsernamePromptSeen =  1;
        System.out.println( "Current Dir: " + System.getProperty( "user.dir" ));
        lines = new Vector();
        lineBuf = new StringBuffer();
        start();
        System.out.println( "End of constructor" );
    }
    
    public vpnConnectThread(vpn3k gui, String account ){
        ui = gui;
        profile = account;
        UsernamePromptSeen =  1;
        System.out.println( "Current Dir: " + System.getProperty( "user.dir" ));
        lines = new Vector();
        lineBuf = new StringBuffer();
        AuthDialog AuthD = new AuthDialog( ui );
        
        if ( ui.getUserID() == null ||
        ui.getTokenPass() == null ||
        ui.ConnectionCancelled == true ){
            ui.Connected = false;
            ui.Connecting = false;
            ui.enableConnectButton();
            ui.stopProgressBar();
            return;
        }
        
        userName = ui.getUserID();
        passwd = ui.getTokenPass();
        ui.enableDisconnectButton();
        start();
        System.out.println( "End of constructor ( vpnConnectThread )" );
    }
    
    public String getUser(){
        return this.userName ;
    }
    public String getPass(){
        return this.passwd ;
    }
    public void closeInputStream(){
        if ( this.in instanceof childConnectionThread ){
            in.closeInput();
        }
    }
    public void closeOutputStream(){
        if ( this.out instanceof childConnectionThread ){
            out.closeOutput();
        }
    }
    public void closeErrorStream(){
        if ( this.err instanceof childConnectionThread ){
            err.closeError();
        }
    }
    public void killProcess(){
        try{
            closeOutputStream();
            closeInputStream();
            closeErrorStream();
            childProcess.destroy();
        } catch ( Exception x ){
        }
    }
    public void endProcess(){
        
        System.out.println( "Waiting for process .." );
        try{
            childProcess.waitFor();
            
        } catch ( Exception intx ){
        }
        System.out.println( "Process ended" );
        if ( ui instanceof vpn3k ) {
            return;
        }
        in.closeInput();
        System.out.println( in.getName() + " has " + in.activeCount() + " threads running and is alive: " + in.isAlive() );
        System.out.println( err.getName() + " has " + err.activeCount() + " threads running and is alive: " + err.isAlive() );
        System.out.println( out.getName() + " has " + out.activeCount() + " threads running and is alive: " + out.isAlive() );
        
        //System.exit(0);
        
    }
    
    public void startConnectionScript(String type){
        System.out.println( "Running connection script " + type );
        if ( type.compareTo(ConnectScriptNames.PostConnectArg) == 0 )
            new runConnectionScript( ConnectScriptNames.PostConnectArg );
        
        
    }
    public void Connect( String acct ){
        profile = acct;
        this.execVpnClient();
        return;
    }
    
    private void execVpnClient( ){
        
        String[] cmd_args = { interpreter, interp_opts, command, profile };
        for ( int i = 0; i<cmd_args.length; i++ ){
            if ( cmd_args[i] == null ){
                System.out.println( "null value for index " + i + " in cmd_args" );
                throw new java.lang.NullPointerException();
            }
        }
        if ( new File( command ).exists() != true ) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No such file " + command );
            return;
        }
        
        boolean newLine = false;
        String lastWord = null;
        
        try {
            // Execute command
            System.out.println( "cmd_args: " + cmd_args );
            childProcess = Runtime.getRuntime().exec(cmd_args);
            
            in = new childConnectionThread( this, childProcess.getInputStream(), "in"  );
            err = new childConnectionThread( this, childProcess.getErrorStream(), "err"  );
            out = new childConnectionThread( this, childProcess.getOutputStream(), "out"  );
            
            
            
        } catch (Exception e) {
            System.out.println( "Error from execVpnClient: " + e.getMessage() );
            e.printStackTrace();
        }
        System.out.println( "End of execVpnClient");
        return;
    }
    
    
    public void writeOutput( String txt ){
        if ( this.out instanceof childConnectionThread )
            this.out.sendOutput( txt );
    }
    public void run() {
        this.Connect(profile);
        System.out.println( "End of vpnConnectThread (run)");
        
        return;
    }
    
    public static void main( String[] args ){
        String arglist = JOptionPane.showInputDialog(null,"Enter profile, id, pass");
        if ( arglist == null || arglist.length() < 1 ) return;
        StringTokenizer st = new StringTokenizer( arglist, " " );
        String[] argArray = new String[3];
        for ( int i=0; i<argArray.length; i++ ){
            argArray[i] = st.nextToken();
        }
        //new vpnConnectThread( args[0], args[1], args[2] );
        new vpnConnectThread( argArray[0], argArray[1], argArray[2] );
        
    }
    protected static  String command = "vpnConnect.exp";
    protected static final String interpreter = System.getProperty( "EXPECT_PATH" );
    protected static final String interp_opts = "-f";
    protected String profile;
    
    
    protected Vector lines = new Vector();
    protected StringBuffer lineBuf = new StringBuffer();
    //String defaultUser = this.readDefaultUser();
    protected String defaultUser = "[First Initial][Last Initial][EmployeeNumber]@vpn";
    
    protected vpn3k ui;
    protected String account;
    protected String userName;
    protected String passwd;
    protected boolean suspendFlag;
    protected Thread connectThread;
    protected int UsernamePromptSeen = 0;
    protected String pathDelim = System.getProperty( "file.separator" );
    protected boolean connecting = false;
    protected boolean connected = false; // connection complete
    protected boolean authenticating = false; // connection authenticating
    protected Process childProcess;
    protected childConnectionThread in;
    protected childConnectionThread out;
    protected childConnectionThread err;
    //protected ProgressThread connectProgress;
    //protected ProgressThread simpleWinConnectProgress;
}


class childConnectionThread extends Thread{
    
    private InputStream in;
    private OutputStream err;
    private OutputStream out;
    private vpnConnectThread vpn;
    private boolean end = false;
    
    public void endThread(){
        end = true;
    }
    
    public childConnectionThread( vpnConnectThread t, InputStream stream, String type ){
        super(type);
        vpn = t;
        if ( type.equals( "in" ))
            in = stream;
        
        start();
        
    }
    public childConnectionThread( vpnConnectThread t, OutputStream stream, String type ){
        super(type);
        vpn = t;
        if ( type.equals( "out" ))
            out = stream;
        else if ( type.equals( "err" ))
            err = stream;
        start();
        
    }
    
    public void sendOutput( String text ){
        try{
            out.write( text.getBytes() );
            out.flush();
        } catch ( Exception x ){}
    }
    public void closeOutput(){
        System.out.println( "Close output stream" );
        try{
            out.flush();
            out.close();
        } catch ( java.io.IOException x ){
        }
    }
    public void closeError(){
        System.out.println( "Close error stream" );
        try{
            err.close();
        } catch ( java.io.IOException x ){
        }
    }
    public void closeInput(){
        System.out.println( "Close input stream" );
        try{
            in.close();
        } catch ( java.io.IOException x ){
        }
    }
    public void run(){
        int c ;
        StringBuffer sb = new StringBuffer();
        try{
            if ( in instanceof InputStream ){
                System.out.println( "Input Stream" );
                while ( ( c=in.read() ) != -1 ){
                    yield();
                    System.out.print( (char)c );
                    if ( (char)c == '\n' ){
                        System.out.flush();
                        if ( this.vpn.ui instanceof vpn3k )
                            if ( sb.indexOf( "Server address: " ) >= 0){
                                
                                Pattern p = Pattern.compile("(.*):([ ]+)([0-9]{1,3}\\.){3}[0-9]{1,3}");
                                String serverLine = sb.toString().trim();
                                //boolean match = Pattern.matches("(.*)Server address:([ ]+)([0-9]{1,3}\\.){3}[0-9]{1,3}",  serverLine);
                                
                                if (p.matcher(serverLine).matches()){
                                    
                                    String[] splitLine = serverLine.split(" ");
                                    String serverIPsplit = splitLine[(splitLine.length - 1)];
                                    String serverIP = serverIPsplit.trim();
                                    if ( this.vpn.ui.StatWindow instanceof StatsWindow ){
                                        System.out.println( "nslookup of -" + serverIP + "-" );
                                        String status = this.vpn.ui.StatWindow.createConnectedStatus(serverIP);
                                        this.vpn.ui.stopProgressBar();
                                        this.vpn.ui.updateStatus(status);
                                    }
                                }
                                
                        /*
                        if ( serverLine.matches("*\\[0-9\\]+\\.\\[0-9\\+\\.\\[0-9\\+\\.\\[0-9\\+") ){
                            System.out.println( "Server address: " + serverLine );
                        }
                         */
                        /*
                        String serverLine = sb.toString();
                        String[] splitLine = serverLine.split(" ");
                         
                        String serverIPsplit = splitLine[(splitLine.length - 1)];
                        String serverIPtrailingDot = serverIPsplit.trim();
                        String serverIP = serverIPtrailingDot.substring(0, (serverIPtrailingDot.length() - 1));
                        if ( this.vpn.ui.StatWindow instanceof StatsWindow ){
                            String status = this.vpn.ui.StatWindow.createConnectedStatus(serverIP);
                            this.vpn.ui.updateStatus(status);
                        }
                         */
                            }
                        this.vpn.ui.updateLog( sb.toString() );
                        sb = new StringBuffer();
                        
                    }
                    sb.append((char)c);
                    if ( sb.indexOf("A connection already exists") >= 0 ){
                        this.vpn.ui.Connected = true;
                        this.vpn.ui.Connecting = false;
                        if ( this.vpn.ui instanceof vpn3k ){
                            if ( sb.lastIndexOf("\n") > 0 )
                                this.vpn.ui.updateLog(sb.toString());
                            //vpn.connectProgress.stopThread();
                            //vpn.simpleWinConnectProgress.stopThread();
                        }
                    }
                    
                    
                    if (    sb.indexOf("Your VPN connection has been terminated.") >= 0 ||
                    sb.indexOf("terminated locally ") >= 0 ||
                    sb.indexOf("The VPN sub-system is busy or has failed") >= 0 ||
                    sb.indexOf("Remote peer is no longer responding") >= 0 ||
                    sb.indexOf("EOF from vpnclient") >= 0 ) {
                        if ( this.vpn.ui instanceof vpn3k ){
                            String status = new String( "Disconnected (vpnconnectThread)" );
                            //this.vpn.ui.stopUpdates();
                            this.vpn.ui.stopProgressBar();
                            this.vpn.ui.updateStatus(status);
                            this.vpn.ui.Connected = false;
                            this.vpn.ui.Connecting = false;
                            //this.vpn.ui.updateLog( sb.toString() );
                            this.vpn.ui.enableConnectButton();
                            
                        }
                        sb = new StringBuffer();
                    }
                    
                    if ( sb.indexOf("Your VPN connection is secure") >= 0 ){
                        if ( this.vpn.ui instanceof vpn3k ){
                            
                            this.vpn.ui.Connected = true;
                            this.vpn.ui.Connecting = true;
                            this.vpn.ui.initialStartup = false;
                            
                            String status = new String( "Connected: Getting Gateway Information .." );
                            this.vpn.ui.updateStatus(status);
                            
                            this.vpn.ui.StatWindow.createConnectedStatus();
                            //vpn.connectProgress.stopThread();
                            //vpn.simpleWinConnectProgress.stopThread();
                            
                            //this.vpn.ui.updateLog( sb.toString() );
                            this.vpn.ui.enableDisconnectButton();
                            new runConnectionScript( ConnectScriptNames.PostConnectArg );
                            //vpn.startConnectionScript(ConnectScriptNames.PostConnectArg);
                            this.vpn.ui.startUpdates();
                        }
                        sb = new StringBuffer();
                    }
                    if ( sb.indexOf("USERNAME:") >= 0 ){
                        String user = new String( vpn.userName + "\n" );
                        sb = new StringBuffer();
                        if ( this.vpn.ui instanceof vpn3k ){
                            if ( vpn.UsernamePromptSeen == 0 ){
                                new AuthDialog( vpn.ui );
                                if ( vpn.ui.getUserID() == null ||
                                vpn.ui.getTokenPass() == null ||
                                vpn.ui.ConnectionCancelled == true ){
                                    vpn.ui.Connected = false;
                                    vpn.ui.Connecting = false;
                                    vpn.ui.enableConnectButton();
                                    vpn.killProcess();
                                    return;
                                }
                            } else {
                                vpn.UsernamePromptSeen = 0;
                            }
                            user = new String( vpn.ui.getUserID() + "\n" );
                            String status = new String( "Connecting to " + this.vpn.profile );
                            this.vpn.ui.updateStatus(status);
                            //this.vpn.ui.updateLog( status );
                            this.vpn.ui.enableDisconnectButton();
                        }
                        vpn.writeOutput( user );
                    }
                    if ( sb.indexOf("Initializing the VPN connection") >=0 ){
                        this.vpn.ui.updateStatus("Connecting ..");
                        this.vpn.ui.Connecting = true;
                        this.vpn.ui.Connected = false;
                    }
                    if ( sb.indexOf("PASSWORD:") >=0 ){
                        String pass = new String( vpn.passwd + "\n" );
                        if ( this.vpn.ui instanceof vpn3k ){
                            pass = new String( vpn.ui.getTokenPass() + "\n" );
                            String status = new String( "Authenticating .." );
                            this.vpn.ui.updateStatus(status);
                        }
                        vpn.writeOutput( pass );
                        
                        sb = new StringBuffer();
                    }
                    
                }
                
                if ( this.vpn.ui.Connected ){
                    this.vpn.ui.setConnectionStatus(true);
                } else {
                    this.vpn.ui.setConnectionStatus(false);
                }
                
                this.vpn.ui.setConnectButton();
                vpn.endProcess();
                
                
            } else if ( err instanceof OutputStream ){
                System.out.println( "Error Stream" );
                vpn=null;
                
            } else if ( out instanceof OutputStream ){
                System.out.println( "Output Stream" );
            }
        } catch ( Exception x ){
            x.printStackTrace();
        }
        System.out.println( "End of run() vpnConnectThread for " + this.getName() );
        if ( this.vpn.ui instanceof vpn3k ){
            //String status = new String( "Disconnected (vpnconnectThread)" );
            //this.vpn.ui.stopUpdates();
            //this.vpn.ui.stopProgressBar();
            //this.vpn.ui.updateStatus(status);
            this.vpn.ui.Connected = false;
            this.vpn.ui.Connecting = false;
            //this.vpn.ui.updateLog( sb.toString() );
            this.vpn.ui.enableConnectButton();
            //this.vpn.ui.setConnectionStatus(false);
            
        }
        
        
    }
}


