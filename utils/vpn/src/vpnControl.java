
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class vpnControl extends Thread {
    
    public vpnControl(String acctProfile, String id, String pass ){
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
    
    public vpnControl(){
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
    
    public vpnControl(vpn3k gui, String account ){
        ui = gui;
        profile = account;
        UsernamePromptSeen =  1;
        UsernamePromptSeen =  0;
        System.out.println( "Current Dir: " + System.getProperty( "user.dir" ));
        lines = new Vector();
        lineBuf = new StringBuffer();
        /*
        new AuthDialog( ui );
        if ( ui.getUserID() == null ||
        ui.getTokenPass() == null ||
        ui.ConnectionCancelled == true ){
            ui.Connected = false;
            ui.Connecting = false;
            ui.enableConnectButton();
            return;
        }
         **/
        ui.enableDisconnectButton();
        start();
        System.out.println( "End of constructor ( vpnControl )" );
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
        
        System.out.println( in.getName() + " has " + in.activeCount() + " threads running and is alive: " + in.isAlive() );
        System.out.println( err.getName() + " has " + err.activeCount() + " threads running and is alive: " + err.isAlive() );
        System.out.println( out.getName() + " has " + out.activeCount() + " threads running and is alive: " + out.isAlive() );
        
        
        
        this.in = null;
        this.err = null;
        this.out = null;
        
        System.out.println( in.getName() + " has " + in.activeCount() + " threads running and is alive: " + in.isAlive() );
        System.out.println( err.getName() + " has " + err.activeCount() + " threads running and is alive: " + err.isAlive() );
        System.out.println( out.getName() + " has " + out.activeCount() + " threads running and is alive: " + out.isAlive() );
        
         //System.exit(0);
        
    }
    public void Connect( String acct ){
        profile = acct;
        //new runConnectionScript( ConnectScriptNames.PreConnectArg );
        this.execVpnClient();
        return;
    }
    
    private void execVpnClient( ){
        
        //String[] cmd_args = { interpreter, interp_opts, command, profile };
        String[] cmd_args = { command };
        for ( int i = 0; i<cmd_args.length; i++ ){
            if ( cmd_args[i] == null ){
                System.out.println( "null value for index " + i + " in cmd_args" );
                throw new java.lang.NullPointerException();
            }
        }
       // if ( new File( command ).exists() != true ) {
       //     JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No such file " + command );
       //     return;
       // }
        
        boolean newLine = false;
        String lastWord = null;
        
        try {
            // Execute command
            System.out.println( "cmd_args: " + cmd_args.toString() );
            Runtime.getRuntime().exec
            childProcess = Runtime.getRuntime().exec(cmd_args);
            //childProcess = Runtime.getRuntime().exec("/opt/COMPvpn/bin/vpnclient connect sfbay");
            
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
        System.out.println( "End of vpnControl (run)");
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
        //new vpnControl( args[0], args[1], args[2] );
        new vpnControl( argArray[0], argArray[1], argArray[2] );
        
    }
    protected static  String command = "vpn.sh";
    protected static  String command_opt = "connect";
    protected static final String interpreter = "/bin/sh";
    protected static final String interp_opts = "-c";
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
    protected int	UsernamePromptSeen = 0;
    protected String pathDelim = System.getProperty( "file.separator" );
    protected boolean connecting = false;
    protected boolean connected = false; // connection complete
    protected boolean authenticating = false; // connection authenticating
    protected Process childProcess;
    protected childConnectionThread in;
    protected childConnectionThread out;
    protected childConnectionThread err;
}


class childConnectionThread extends Thread{
    
    private InputStream in;
    private InputStream err;
    private OutputStream out;
    private vpnControl vpn;
    private boolean end = false;
    
    public void endThread(){
        end = true;
    }
    
    public childConnectionThread( vpnControl t, InputStream stream, String type ){
        super(type);
        vpn = t;
        if ( type.equals( "in" ))
            in = stream;
        else if ( type.equals( "err" ))
            err = stream;
        start();
        
    }
    public childConnectionThread( vpnControl t, OutputStream stream, String type ){
        super(type);
        vpn = t;
        if ( type.equals( "out" ))
            out = stream;
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
                            this.vpn.ui.updateLog( sb.toString() );
                        sb = new StringBuffer();
                        
                    }
                    sb.append((char)c);
                    
                    if (    sb.indexOf("Your VPN connection has been terminated.") >= 0 ||
                    sb.indexOf("terminated locally ") >= 0 ) {
                        if ( this.vpn.ui instanceof vpn3k ){
                            String status = new String( "Disconnected" );
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
                            String status = new String( "Connected to " + this.vpn.profile );
                            this.vpn.ui.updateStatus(status);
                            this.vpn.ui.Connected = true;
                            this.vpn.ui.Connecting = true;
                            //this.vpn.ui.updateLog( sb.toString() );
                            this.vpn.ui.enableDisconnectButton();
                            new runConnectionScript( ConnectScriptNames.PostConnectArg );
                            // this.vpn.ui.startUpdates();
                        }
                        sb = new StringBuffer();
                    }
                    if ( sb.indexOf("Username") >= 0 && sb.indexOf(":") >= 0 ){
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
                vpn.endProcess();
                
              
             } else if ( err instanceof InputStream ){
                System.out.println( "Error Stream" );
                while ( ( c=err.read() ) != -1 ){
                    System.out.print( (char)c );
                    System.out.flush();
                    yield();
                }
                //vpn.closeErrorStream();
                vpn=null;
                
            } else if ( out instanceof OutputStream ){
                System.out.println( "Output Stream" );
            }
        } catch ( Exception x ){}
        System.out.println( "End of run() vpnControl for " + this.getName() );
        
    }
}


