
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;


public class vpnNotify extends Thread {
    
    // options
    //
    //  stat [reset] [traffic] [tunnel] [route] [repeat]

    public vpnNotify(){
        lines = new Vector();
        lineBuf = new StringBuffer();
        
        suspendFlag = false;
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        start();
        System.out.println("End of vpnNotify (1)" );
    }
    public vpnNotify(NotifyWindow win){
        Win=win;
        lines = new Vector();
        lineBuf = new StringBuffer();
        
        suspendFlag = false;
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        start();
        System.out.println("End of vpnNotify (1)" );
    }
    
    public vpnNotify(String opt){
       option = opt;
        lines = new Vector();
        lineBuf = new StringBuffer();
       
        suspendFlag = false;
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        start();
        System.out.println("End of vpnNotify (1)" );
    }
   
    public vpnNotify(vpn3k vpnUI, String opt){
        
        try {
            Thread.currentThread().sleep( 1000 );
        } catch ( java.lang.InterruptedException x  ){
            x.printStackTrace();
        }
        option=opt;
        UI = vpnUI;
        lines = new Vector();
        
        lineBuf = new StringBuffer();
        suspendFlag = false;
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        start();
        System.out.println("End of vpnNotify (vpn3k)" );
    }
   
    public vpnNotify(vpn3k vpnUI){
        
        try {
            Thread.currentThread().sleep( 1000 );
            Thread.currentThread().yield();
        } catch ( java.lang.InterruptedException x  ){
            x.printStackTrace();
        }

        UI = vpnUI;
        lines = new Vector();
        lineBuf = new StringBuffer();
        suspendFlag = false;
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        start();
        System.out.println("End of vpnNotify (vpn3k)" );
    }
   
   private void printNotification(){
       
       Enumeration el = lines.elements();
       while ( el.hasMoreElements() ){
           System.out.println( el.nextElement()  );
       }
       
    }
   protected String[] getNotificationInfo(){
       
       
       Object[] info = this.lines.toArray();
       String[] infoArray = new String[info.length];
       for ( int i=0; i<info.length; i++){
           yield();
           if ( info[i] instanceof String ){
               String x = (String)info[i];
               infoArray[i] = x;
           }
       }
       return infoArray;
   }
   
   protected void getNotification( ){
       
       Vector CmdList = new Vector();
       CmdList.add(interpreter);
       CmdList.add(interp_opts);
       CmdList.add( command );
       
       
        if ( option.compareTo("reset") == 0 ){
            
            CmdList.add( option );
            
        }
        System.out.println( "Command: " + CmdList.toString() );
        String[] CommandArgs = new String[ CmdList.size() ];
        for ( int i=0; i<CommandArgs.length; i++ ){
            CommandArgs[i] = (String)CmdList.elementAt(i);
        }
        
        //String[] cmd_args = { interpreter, interp_opts, command };
        
        if ( new File( command ).exists() != true ) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No such file " + command );
          	return; 
        }
        
        boolean newLine = false;
        String lastWord = null;
        
        try {
            // Execute command
            
            Process child = Runtime.getRuntime().exec(CommandArgs);
            
            // Get input stream to read from it
            InputStream in = child.getInputStream();
            OutputStream out = child.getOutputStream();
            
            
            InputStreamReader is = new InputStreamReader( in );
            BufferedReader br = new BufferedReader( is );
            
           
            
            //String response =  JOptionPane.showInputDialog( new javax.swing.JFrame(), "Enter Password: " );
            //String password = response.concat("\n");
            int c;
            int count = 0;
            int lastchar = 0;
            
            String currentHeading = "";
            boolean firstTime = true;
            String Line;
            String lastLine;
            boolean loopFlag = true;
            boolean dataCollected = false;
            
            while ( loopFlag ) {
                
               
               Line = br.readLine();
               if ( Line == null ){
                   break;
               } else if ( Line.length() < 1 ){
                    continue;
               } else if ( Line.startsWith("End of vpnStat") ){
                   break;
               } else if ( Line.startsWith("Your VPN connection is not active") ){
                   break;
               } else {
                   
                   lines.add(Line);
               }
                
                Thread.yield();
                
                // check if line contains heading
                boolean continueFlag = false;
                if ( continueFlag == true ){
                    continue;
                 }
                
            }
            
            br.close();
            is.close();
            in.close();
	    child.waitFor();
            
            //System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main( String[] args ){
        
        
        
        vpnNotify notification = new vpnNotify();
        try {
            notification.join();
            
        } catch ( InterruptedException ix ){
            System.out.println( ix.getMessage() );
        }
       //notification.printNotification();
       String[] info = notification.getNotificationInfo();
       for ( int i=0; i< info.length; i++ ){
            System.out.println( info[i] );
       }
        //System.out.println( "Info requested: " + my
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
        this.getNotification();
        // populate vpn3k table
        //this.printNotification();
         //remoteRequest = this.getInfo();
         
        if ( this.Win instanceof NotifyWindow ){
            Win.appendTextArray(this.getNotificationInfo());
            
        } 
        System.out.println ( "End of vpnNotify (2)" );
       
        
    }
    private String pathDelim = System.getProperty( "file.separator" );
    private final String command = System.getProperty("user.dir" ) + pathDelim + "vpnNotify.exp";
    //private static  String command = "/home/pdel/VPN3K/vpnDisconnect.exp";
    //private static final String interpreter = "/usr/bin/expect";
    private static final String interpreter = System.getProperty( "EXPECT_PATH" ); 
    private static final String interp_opts = "-f";
    private String profile;
    private Vector lines = new Vector();
    private StringBuffer lineBuf = new StringBuffer();
    public vpn3k UI;
    boolean suspendFlag;
    protected Thread statusThread;
   private String option = "";
   protected NotifyWindow Win;
    
}

/*
 Cisco Systems VPN Client Version 4.0 (Rel)
Copyright (C) 1998-2003 Cisco Systems, Inc. All Rights Reserved.
Client Type(s): Linux
Running on: Linux 2.4.18-24.8.0 #1 Fri Jan 31 06:51:30 EST 2003 i686

VPN tunnel information.
Connection Entry: Central gateway
Client address: 129.150.32.11
Server address: 192.18.98.40
Encryption: 168-bit 3-DES
Authentication: HMAC-MD5
IP Compression: None
NAT passthrough is active on port TCP 90
Local LAN Access is enabled

VPN traffic summary.
Time connected: 0 day(s), 00:01.30
Bytes in: 1851
Bytes out: 656
Packets encrypted: 4
Packets decrypted: 4
Packets bypassed: 11
Packets discarded: 1

Configured routes.
Secured    Network Destination   Netmask
           0.0.0.0               0.0.0.0

Local      Network Destination   Netmask
           192.168.1.0           255.255.255.0

 **/
