import javax.naming.*;
import javax.naming.directory.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;
import java.net.*;

public class nslookup{
    
    private Hashtable env = new Hashtable();
    //129.147.2.21
    //private static final String dnsserver = "brm-tier2-dns-1.Central.Sun.COM";
    private static String dnsserver = "129.147.2.21";
    private DirContext ctx;
    private Object obj;
    private Attributes attrs;
    private String hostname;
    private boolean isIPAddress = false;
    private String reverseIPAddress;
    private Vector records = new Vector();
    private boolean guiOn = false;
    
    public nslookup(String host){
        hostname = host;
        setEnv();
        getRecords();
    }
    
    public Vector lookup( String host ){
        hostname=host;
        records.clear();
        setEnv();
        getRecords();
        return records;
    }
    public nslookup(){
       
    }
    public void turnOnGui(){
        guiOn = true;
    }
    public void turnOffGui(){
        guiOn = false;
    }
    public void setDnsServer( String server ){
        dnsserver = server;
    }
    private void doLookups(){
        boolean loop = true;
        hostname = "";
        while ( true ){
            try{
            hostname = JOptionPane.showInputDialog( null, "Enter Hostname ( Host.Domain or IP address) : " );
            if ( hostname.length() == 0 ) break;
            } catch ( NullPointerException np ){
               break;
            }
           
            getRecords();
        }
     }
    
    private String reverseIP(String ip){
        String[] ipSplit = ip.split("\\.");
        // 0,1,2,3
        // 3,2,1,0
        if ( ipSplit.length == 4 ){
            return new String( ipSplit[3] + "." + ipSplit[2] + "." + ipSplit[1] + "." + ipSplit[0] );
        }
        return "0.0.0.0";
    }
    
    public Vector getResults(){
        return records;
    }
    private void getRecords(){
        try {
            ctx = new InitialDirContext(this.env);
            if ( isIPAddress( hostname )){
                String arpaAddr;
                reverseIPAddress = reverseIP( hostname );
                arpaAddr = reverseIPAddress + ".IN-ADDR.ARPA";
                attrs = ctx.getAttributes(arpaAddr, new String[]{"PTR"});
            } else {
                attrs = ctx.getAttributes(hostname, new String[]{"A"});
            }
            
        } catch (NamingException e) {
            System.out.println( "Naming exception: " + e.getExplanation() );
             String msg = new String( "Host not found: \n" +  e.getExplanation());
            if ( guiOn ) JOptionPane.showMessageDialog(null, msg );
            return;
        } catch ( java.lang.NullPointerException np ){
            return;
        }
        
        for ( NamingEnumeration en = attrs.getAll(); en.hasMoreElements(); ){
            Attribute x = (Attribute)en.nextElement();
            for ( int i=0; i<x.size(); i++ ){
                try {
                    records.add( x.get(i) );
                } catch ( NamingException nx ){
                }
            }
            //System.out.println( "Attribute: " + x );
            String msg = new String( "Attribute for " + hostname + ": \n" + x );
            if ( guiOn ) JOptionPane.showMessageDialog(null, msg );
        }
        
    }
    
    private boolean isIPAddress(String host){
        
        String[] splitHost = host.split("\\.");
        Integer num;
        for ( int i=0; i < splitHost.length; i++ ){
            try{
                num =  Integer.decode(splitHost[i]);
            } catch ( java.lang.NumberFormatException x ){
                return false;
            }
        }
        return true;
    }
    
    private void setEnv(){
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url",    "dns://" + dnsserver + "/.");
        //env.put(java.naming.directory.Context.AUTHORITATIVE, "false");
        env.put("com.sun.jndi.dns.timeout.initial", "10000");
        env.put("com.sun.jndi.dns.timeout.retries", "3");
        
    }
    
    
    public static void main( String[] args ){
        
        if ( args.length == 1 ){
            new nslookup(args[0]);
            
        }else{
            
            nslookup ns = new nslookup();
            Vector results = ns.lookup("ivpn-central.sun.com");
            if ( results.size() > 0 )
            System.out.println( "Lookup result: " + results.get(0) );
            results =  ns.lookup("192.18.98.40");
            if ( results.size() > 0 )
            System.out.println( "Lookup result: " + results.get(0) );
        }
        System.exit(0);
    }
    
    
    
}
