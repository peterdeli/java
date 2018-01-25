/*
 * connectStatus.java
 *
 * Created on September 23, 2003, 3:47 PM
 */

/**
 *
 * @author  pdel
 *
 *
 *Get:
    connection type
    serial,ethernet,etc
    router addr if any
    network config
    user login ( root, user )

 */

import java.net.*;
import java.util.*;

public class NetInterfaces{
    public NetInterfaces(){
        this.listAll();
    }
    
    public Hashtable listAll(){
        return this.getInterfaces();
        
    }
    public String getIP ( String ifName ){
        
        return ( this.interfaceList.containsKey(ifName)? this.interfaceList.get(ifName).toString() : null );
       
    }
    
    public Vector getIF ( String IP ){
        
        Enumeration enumer = this.interfaceList.keys();
        String if_name = null;
        Vector ip = null;
        String key = null;
        boolean found_if = false;
        Vector if_names = new Vector();
        
        while ( enumer.hasMoreElements() ){
            
            key = enumer.nextElement().toString();
            ip = (Vector)this.interfaceList.get(key);
            
            for ( int i=0; i<ip.size(); i++ ){
                
                if ( (ip.elementAt(i).toString().compareTo(IP)) == 0 ){
                    found_if = true;
                    if_names.add ( key );
                }
            }
        }
       return ( found_if == true ? if_names : null );
    }
    // if -> name->name
    //    -> IP address -> IP address
    private Hashtable getInterfaces(){
        
      
        String interfaceName = null;
        Hashtable interfaces = new Hashtable();
        try {
            Enumeration enumer = NetworkInterface.getNetworkInterfaces();
          
            while ( enumer.hasMoreElements() ){
                Vector IPAddrs = new Vector();
                NetworkInterface ne = (NetworkInterface)enumer.nextElement();
                String ifName = ne.getDisplayName();
                //System.out.println ( "IF: " + ifName );
                Enumeration ipAddrs = ne.getInetAddresses();
                while ( ipAddrs.hasMoreElements() ){
                    String ip = ipAddrs.nextElement().toString();
                    //System.out.println ( "ip is type " + ip.getClass() );
                    if ( ip.startsWith("/") ){
                        String addr = ip.substring(1);
                        IPAddrs.add( addr );
                    }
                    //System.out.println( "IP: " + ip.toString() );
                }
                // add to list
                //System.out.println ( "IF: " + ifName + " IP: " + IPAddrs );
                interfaces.put(ifName,  IPAddrs );
                
            }
            
        } catch ( SocketException se ){
            se.printStackTrace();
        }
        this.interfaceList = interfaces;
        return interfaces;
        
    }
    
    public static void main(String[] args) {
        //new connectStatus();
        System.out.println ( "Interfaces on this system" );
        NetInterfaces ni = new NetInterfaces();
        Hashtable ht = ni.listAll();
        Enumeration en = ht.keys();
        while ( en.hasMoreElements() ){
            String interface_name = en.nextElement().toString();
            Vector interface_ip = (Vector)ht.get(interface_name);
            
            System.out.println ( "Name: " + interface_name );
            
            for ( int i=0; i<interface_ip.size(); i++ ){
                System.out.println( "IP: " + interface_ip.elementAt(i) );
            }
        }
        //System.out.println( ni.listAll() );
        //System.out.println( "hme0 IP addresses: " + ni.getIP( "hme0" ) );
        System.out.println( "IF name of 127.0.0.1: " + ni.getIF( "127.0.0.1" ) );

    }
    public Hashtable interfaceList;
    
}
