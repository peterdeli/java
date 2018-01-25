/*
 * NetStat.java
 *
 * Created on December 18, 2003, 1:47 PM
 */

/**
 *
 * @author  Peter Delevoryas
 */

import java.util.*;
import java.net.*;
import java.io.*;

public class NetStat {
    WinPing Host = new WinPing();
    String DefaultVpnServer = "ivpn-central.sun.com";
    int DefaultVpnPort = 80;
    /** Creates a new instance of NetStat */
    
    
    public boolean  checkConnection(String hostname){
       return Host.isAlive(hostname);
     }
    
    private void init(){
        
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    
        System.out.println ( "Checking connection .." );
        NetStat stat = new NetStat();
        //- First, verify that the user is not already connected to the VPN/SWAN
        if ( stat.checkConnection("one.central.sun.com") == true ){
            System.out.println( "Connection to SWAN OK" );
        } else {
            
            System.out.println ( "No Connection to SWAN, checking network configuration .." );
            
            //- Computer's network interface physically up?
            parseIPConfig ipc = new parseIPConfig("/all");
            Hashtable ipcInfo = ipc.getInfo();
            // an 'up' interface will have an IP # and a gateway
            
           // get IP address and gateway
            Object lanInfoObject = ipcInfo.get( HeadingFields.LanHeading );
            // contains Hashtable pointing to vector of items
            Hashtable lanInfo = new Hashtable();
            if ( lanInfoObject instanceof Hashtable ){
                lanInfo = (Hashtable)lanInfoObject;
                if ( lanInfo.size() < 1 ) return;
            }
            int size = lanInfo.size();
            //"Media State . . . . . . . . . . . : Media disconnected" ??
            if ( lanInfo.containsKey( HeadingFields.MediaState ) ){
                Vector MediaState = (Vector)lanInfo.get( HeadingFields.MediaState );
                String state = MediaState.get(0).toString().trim();
                if ( state.startsWith("Media disconnected") ){
                    System.out.println( "Network cable unplugged, check connections" );
                    return;
                }
            }
            
            Vector IPAddress = (Vector)lanInfo.get( HeadingFields.IPAddress );
            Vector DefaultGW = (Vector)lanInfo.get( HeadingFields.DefaultGateway );
            Vector NetMask = (Vector)lanInfo.get( HeadingFields.SubnetMask );
            Vector DnsServers = (Vector)lanInfo.get( HeadingFields.DNSServers );
            
            System.out.println( "\n" + "IP address: " + (String)IPAddress.get(0) + "\n" + 
            "Default Gateway: " + (String)DefaultGW.get(0)  + "\n" +
            "Netmask: " + (String)NetMask.get(0) + "\n" );
            
            String DefaultGateway = (String)DefaultGW.get(0);
            //- Connectivity to their default router (DSL/cablemodem)?
            if ( stat.Host.isAlive(DefaultGateway ) ){
                System.out.println( "Connection to Default Gateway " + DefaultGateway + " OK" );
                // Check for host on network
                
                /*
                System.out.println( "Checking ISP connection .. pinging DNS server" );
                if ( stat.Host.isAlive( (String)DnsServers.get(0) ) ){
                    System.out.println( "Ping of ISP DNS server: " + (String)DnsServers.get(0) + " is alive" );
                    
                } else {
                    System.out.println( "Ping of ISP DNS server: " + (String)DnsServers.get(0) + " not responding or host not found" );
                }
                 **/
                
                 System.out.println( "Checking Connection to vpn server " + stat.DefaultVpnServer );
                 boolean connectOk = false;
                 try {
                     Socket vpnSocket = new Socket( stat.DefaultVpnServer, stat.DefaultVpnPort );
                     System.out.println( "Connectivity to " + stat.DefaultVpnServer + " OK" );
                     connectOk = true;
                     vpnSocket.close();
                 } catch ( UnknownHostException uh  ){
                     System.out.println( "No connection to " + stat.DefaultVpnServer + ": Host Unknown" );
                     //uh.printStackTrace();
                     System.out.println ( uh.getLocalizedMessage() );
                 } catch ( IOException iox ){
                     if ( connectOk == false ){
                         System.out.println( "No connection to " + stat.DefaultVpnServer  + ": IO Exception" );
                         System.out.println ( iox.getLocalizedMessage() );
                         //iox.printStackTrace();
                     }
                 }
                
                 
                
                
                 return;
            } else {
                System.out.println( "No Connection to Default Gateway " + DefaultGateway);
                System.out.println( "Check connection to Default Gateway" );
                System.out.println( "Netmask: " + (String)NetMask.get(0) );
           }
            
           
        }
        
    }

    
    
}
