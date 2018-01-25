
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;


public class vpnStat extends Thread {
    
    // options
    //
    //  stat [reset] [traffic] [tunnel] [route] [repeat]

    public vpnStat(){
        lines = new Vector();
        lineBuf = new StringBuffer();
        configureRouteMapping();
        suspendFlag = false;
        option = "stat";
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        start();
        System.out.println("End of vpnStat (1)" );
    }
    
   public vpnStat(String opt){
       option = opt;
        lines = new Vector();
        lineBuf = new StringBuffer();
        configureRouteMapping();
        suspendFlag = false;
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        start();
        System.out.println("End of vpnStat (1)" );
    }
   
   public vpnStat(vpn3k vpnUI, String opt){
        
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
        interpreter = UI.getInterpreter();
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        start();
        System.out.println("End of vpnStat (vpn3k)" );
    }
   
    public vpnStat(vpn3k vpnUI){
        
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
        System.out.println("End of vpnStat (vpn3k)" );
    }
   
   private void printStatus(){
       
       Set keys = statusItems.keySet();
       Object[] keyArray = keys.toArray();
       
       for ( int i=0; i<keyArray.length; i++ ){
           System.out.println ( (String)keyArray[i] + ": " + statusItems.get(keyArray[i]) );
           
       }
    }
   protected Hashtable getInfo(){
        return statusItems;
   }
   
   protected void getStatus( ){
       
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
            Hashtable items = new Hashtable();
            boolean securedRouteFound = false;
            boolean localRouteFound = false;
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
                   statusItems.put("Status", "inactive" );
                    break;
               } else if ( Line.startsWith("The VPN sub-system is busy or has failed" )){
                   statusItems.put("Status", "busy/failed" );
                   if ( UI instanceof vpn3k ){
                        UI.updateStatus( Line );
                   }
                    break;
               }
                
                Thread.yield();
                
                // check if line contains heading
                boolean continueFlag = false;
                for ( int i=0; i<statHeadings.length; i++ ){
                     Thread.yield();
                    if ( Line.startsWith(statHeadings[i]) ){
                        // start of new section
                        if ( i > 0 ){
                            // end previous section
                            if ( currentHeading != null  && items != null ){
                                statusItems.put( currentHeading, items );
                                items = new Hashtable();
                            }
                        }
                        currentHeading = statHeadings[i];
                         statusItems.put("Status", "active" );
                        continueFlag = true;
                        break;
                    }
                }
               
                 if ( continueFlag == true ){
                    continue;
                 }
                
                // last entry is configured routes
                
                // look for key/values under current heading
                if ( currentHeading.compareTo(statHeadings[0]) == 0 ){
                    // check for key
                    for ( int i=0; i<tunnelKeys.length; i++ ){
                         Thread.yield();
                        if ( Line.startsWith(tunnelKeys[i]) ){
                            // found one
                            //split it except for 6 or 7
                            //NAT passthrough is active on port TCP 90
                            //Local LAN Access is enabled

                            if ( i == 6 ){
                                // NAT
                                String [] splitLine = Line.split( " " );
                                
                                Pattern p = Pattern.compile( "is active" );
                                Matcher m = p.matcher(Line);
                                if ( m.find() ){
                                    items.put( tunnelKeys[i], "active" );
                                } else {
                                    items.put( tunnelKeys[i], "disabled" );
                                }
                                continue;
                            } else if ( i == 7 ){
                                // Local LAN
                                //Local LAN Access
                                 String [] splitLine = Line.split( " " );
                                
                                Pattern p = Pattern.compile( "enabled" );
                                Matcher m = p.matcher(Line);
                                if ( m.find() ){
                                    items.put( tunnelKeys[i], "enabled" );
                                } else {
                                    items.put( tunnelKeys[i], "disabled" );
                                }
                                continue;
                            } else {
                                String[] keyValue = Line.split(":", 2);
                                
                                items.put(keyValue[0].trim(), keyValue[1].trim() );
                                break;
                            }
                        }
                    }
                } else if ( currentHeading.compareTo(statHeadings[1]) == 0 ){
                    // check for key
                    for ( int i=0; i<trafficKeys.length; i++ ){
                         Thread.yield();
                        if ( Line.startsWith(trafficKeys[i]) ){
                            // found one
                            //split it
                            String[] keyValue = Line.split(":", 2);
                            
                            items.put(keyValue[0].trim(), keyValue[1].trim() );
                            break;
                        }
                    }
                } else if ( currentHeading.compareTo(statHeadings[2]) == 0 ){
                    // check for key
                     if ( securedRouteFound == true ){
                    //      trim it
                    //      split on whitespace
                    //      trim each item
                    //      first is destination, second is netmask
                         String trimmedLine = Line.trim();
                         String[] splitLine = trimmedLine.split(" ");
                         String route = splitLine[0];
                         String netmask = splitLine[ splitLine.length - 1];
                         
                         String routeKey = routeTypes[0] + " " + routeKeys[0];
                         String netmaskKey = routeTypes[0] + " " + routeKeys[1];
                         items.put(routeKey, route);
                         items.put( netmaskKey, netmask );
                         securedRouteFound = false;
                         continue;
                    } else if ( localRouteFound == true ){
                    //      same as above
                        String trimmedLine = Line.trim();
                         String[] splitLine = trimmedLine.split(" ");
                         String route = splitLine[0];
                         String netmask = splitLine[ splitLine.length - 1];
                         
                         String routeKey = routeTypes[1] + " " + routeKeys[0];
                         String netmaskKey = routeTypes[1] + " " + routeKeys[1];
                         items.put(routeKey, route);
                         items.put( netmaskKey, netmask );
                        localRouteFound = false;
                        statusItems.put( currentHeading, items );
                        dataCollected = true;
                        continue;
                    }
                    
                    for ( int i=0; i<routeTypes.length; i++ ){
                         Thread.yield();
                        if ( Line.startsWith(routeTypes[i]) ){
                            // found one
                            //this is the 'heading' Secured -- Network Destination -- Netmask
                            // so we set a flag and catch it for the next line
                            // 
                            if ( i == 0 ){
                               securedRouteFound = true;
                               break;
                            } else if ( i == 1 ){
                                localRouteFound = true;
                                break;
                            }
                            //items.put(keyValue[0], keyValue[1] );
                            break;
                        }
                    }
                }
            } // end of while()
            
            
            br.close();
            is.close();
            in.close();
	    child.waitFor();
            
         } catch ( java.lang.InterruptedException ie ){
            //System.exit(0);
        } catch (java.lang.NullPointerException e) {
            System.out.println( "Null pointer returned from process" );
            e.printStackTrace();
        } catch ( java.io.IOException ioe ){
            System.out.println( "IO Exception from process" );
            ioe.printStackTrace();
        }
        
    }
    
    public static void main( String[] args ){
        
        Hashtable ht = new Hashtable();
        vpnStat mystatus = new vpnStat();
        try {
           mystatus.join();
        } catch ( InterruptedException ix ){
            System.out.println( ix.getMessage() );
        }
        ht = mystatus.remoteRequest;
        //System.out.println( "Info requested: " + mystatus.remoteRequest );
        System.out.println( "Info requested: " + ht );
    }
    
    
    
    private void configureRouteMapping(){
        routeMappings = new Hashtable();
        for ( int i=0; i<routeTypes.length; i++){
             Thread.yield();
            routeMappings.put(routeTypes[i], routeKeys);
        }
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
        this.getStatus();
        // populate vpn3k table
         this.printStatus();
         remoteRequest = this.getInfo();
         
        if ( this.UI instanceof vpn3k ){
            this.UI.Stats = this.getInfo();
        } 
        System.out.println ( "End of vpnStat (2)" );
       
        
    }
    private String pathDelim = System.getProperty( "file.separator" );
    private final String command = System.getProperty("user.dir" ) + pathDelim + "vpnStat.exp";
    //private static final String interpreter = "/usr/bin/expect";
    private String interpreter = System.getProperty( "EXPECT_PATH" ); 
    private static final String interp_opts = "-f";
    private String profile;
    private Vector lines = new Vector();
    private StringBuffer lineBuf = new StringBuffer();
    public vpn3k UI;
    boolean suspendFlag;
    protected Thread statusThread;
    protected static final String[] statHeadings = {
        "VPN tunnel information",
        "VPN traffic summary",
        "Configured routes"
    };
    
    protected static final String[] tunnelKeys = { 
        "Connection Entry", 
        "Client address", 
        "Server address",
        "Encryption",
        "Authentication",
        "IP Compression",
        "NAT passthrough",
        "Local LAN Access"
    };
    
    protected static final String[] trafficKeys = {
        "Time connected",
        "Bytes in",
        "Bytes out",
        "Packets encrypted",
        "Packets decrypted",
        "Packets bypassed",
        "Packets discarded"
     };
     private  Hashtable routeMappings;
     protected static final String[] routeKeys = {
         "Network Destination",
         "Netmask"
         
     };
     protected static final String[] routeTypes = {
        "Secured",
        "Local"
         
     };
     
    private Hashtable tunnelInfo;
    public Hashtable statusItems = new Hashtable();
    public Hashtable remoteRequest = new Hashtable();
    private Hashtable statMapping = new Hashtable();
    private String option = "";
    
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
