
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;


public class parseRoute extends Thread {
    
    // options
    //
    //  stat [reset] [traffic] [tunnel] [route] [repeat]
    
    public parseRoute(){
        lines = new Vector();
        lineBuf = new StringBuffer();
        suspendFlag = false;
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        
        start();
        System.out.println( "Please wait .. " );
        try {
            join();
        } catch ( java.lang.InterruptedException xyz ){
        }
        System.out.println( "Route Configuration generated" );
        System.out.println("End of parseRoute(1)" );
    }
    
    public parseRoute(String opt){
        option = opt;
        lines = new Vector();
        lineBuf = new StringBuffer();
        
        suspendFlag = false;
        
        //statusThread = new Thread( this, "vpnclient status thread" );
        //statusThread.start();
        start();
        System.out.println( "Please wait .. " );
        try {
            join();
        } catch ( java.lang.InterruptedException xyz ){
        }
        System.out.println( "Configuration generated" );
        System.out.println("End of parseRoute (1)" );
    }
    
    public Hashtable gatewayInfo() {
       return this.gateways;
    }
    
    private void printConfig(){
        
        Set keys = gateways.keySet();
        Iterator it = keys.iterator();
        Hashtable infoHash = null;
        while ( it.hasNext() ){
            Object obj = it.next();
            System.out.println( "Gateway " + obj );
            Object info = gateways.get( obj );
            if ( info instanceof Hashtable ){
                infoHash = (Hashtable)info;
            }
            Enumeration en = infoHash.keys();
            while ( en.hasMoreElements() ){
                Object key = en.nextElement();
                System.out.println( "Key: [" + key + "] Value: [" + infoHash.get( key ) + "]" );
            }
        }
        
    }
    
    protected Hashtable getInfo(){
        return gateways;
    }
    
    
    
    protected String getPathtoExe( String name ){
        String systemPaths = "/bin:/usr/bin:/usr/sbin:/sbin:/usr/local/bin";
        String validPath = "";
        
        String[] paths = systemPaths.split(":");
        
        for ( int i=0; i<paths.length; i++ ){
            String testpath = new String( paths[i] + System.getProperty("file.separator") + name );
            if ( new File( testpath ).exists() ){
                validPath = testpath;
                break;
            }
        }
        if ( validPath.length() < 1 ) {
            System.out.println( name + " not found on any path" );
        } else {
            System.out.println( "Path to " + name + ": " + validPath  );
        }
        return validPath;
    }
    
    
    protected void getConfig(){
        
        Vector CmdList = new Vector();
        CmdList.add( command );
        if ( option.compareTo("-rn") == 0 ){
            CmdList.add( option );
        }
        System.out.println( "Command: " + CmdList.toString() );
        String[] CommandArgs = new String[ CmdList.size() ];
        for ( int i=0; i<CommandArgs.length; i++ ){
            CommandArgs[i] = (String)CmdList.elementAt(i);
        }
        
        //String[] cmd_args = { interpreter, interp_opts, command };
        if ( command.length() < 1 ) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), netstat_basename + " not found on system path."  );
            return;
        }
        if ( new File( command ).exists() != true ) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No such file " + command );
            return;
        }
        
        
        /**
         * bash-2.05b$ netstat -rn
         * Kernel IP routing table
         * Destination     Gateway         Genmask         Flags   MSS Window  irtt Iface
         * 192.168.1.0     0.0.0.0         255.255.255.0   U        40 0          0 eth0
         * 127.0.0.0       0.0.0.0         255.0.0.0       U        40 0          0 lo
         * 0.0.0.0         192.168.1.1     0.0.0.0         UG       40 0          0 eth0
         *
         **/
        try {
            // Execute command
            
            Process child = Runtime.getRuntime().exec(CommandArgs);
            // Get input stream to read from it
            InputStream in = child.getInputStream();
            InputStreamReader is = new InputStreamReader( in );
            BufferedReader br = new BufferedReader( is );
            OutputStream out = child.getOutputStream();
            
            boolean loopFlag = true;
            String adapter = null;
            boolean foundGatewayLine = false;
            boolean gatewayUp = false;
            String IP = null;
            String line = "";
            Hashtable gatewayInfo = new Hashtable();
            // gateway hash
            // key is IP
            // value is Hash of attributes
            // State -> up/down
            // adapter name -> eth[0-9], etc.
            
            while ( true ) {
                line = br.readLine();
                if ( line == null ) break;
                // get line, split, 4th col should be "UG"
                String[] splitLine = line.split("[ \t]+");
                
                if ( splitLine.length == 8 ){
                    // Look for "UG"
                    if ( Pattern.matches( LinuxHeadingFields.NetstatUpGateway, splitLine[3] ) ){
                        gatewayInfo.put( LinuxHeadingFields.GatewayInterface,  splitLine[7] );
                        gatewayInfo.put( LinuxHeadingFields.GatewayStateKey, LinuxHeadingFields.UP );
                        // IP Number
                        this.gateways.put( splitLine[1],  gatewayInfo );
                    }
                }
            }
            in.close();
            out.close();
            System.out.println( "Ifconfig Parsed" );
            //System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main( String[] args ){
        
        Hashtable hashData = new Hashtable();
        parseRoute route = new parseRoute(args[0]);
        System.out.println( "Using Route command: " + route.command );
        System.out.println( "Info ready" );
        hashData = route.getInfo();
        Enumeration en = hashData.keys();
        while ( en.hasMoreElements() ){
            Object key = en.nextElement();
            System.out.println( key + " = " + hashData.get(key) );
        }
        
        route.printConfig();
        //System.out.println( "Info requested: " + ht );
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
        this.getConfig();
        // populate vpn3k table
        //this.printConfig();
        //remoteRequest = this.getInfo();
        System.out.println( "End of parseIFConfig run()" );
    }
    
    protected static final String[] LinuxIfconfigFields = {
        LinuxHeadingFields.LinkEncap,
        LinuxHeadingFields.HwAddr,
        LinuxHeadingFields.InetAddr,
        LinuxHeadingFields.Bcast,
        LinuxHeadingFields.Mask,
        LinuxHeadingFields.MTU,
        LinuxHeadingFields.Metric,
        LinuxHeadingFields.RxPackets,
        LinuxHeadingFields.RxPacketErrors,
        LinuxHeadingFields.RxPacketDropped,
        LinuxHeadingFields.RxPacketOverruns,
        LinuxHeadingFields.RxPacketFrame,
        LinuxHeadingFields.TxPackets,
        LinuxHeadingFields.TxPacketErrors,
        LinuxHeadingFields.TxPacketDropped,
        LinuxHeadingFields.TxPacketOverruns,
        LinuxHeadingFields.TxPacketCarrier,
        LinuxHeadingFields.Collisions,
        LinuxHeadingFields.TxQueueLen,
        LinuxHeadingFields.RxBytes,
        LinuxHeadingFields.TxBytes
        
    };
    protected static final String[] UnderscoreReplacementTargets = {
        LinuxHeadingFields.LinkEncap,
        LinuxHeadingFields.InetAddr,
        LinuxHeadingFields.RxBytes,
        LinuxHeadingFields.TxBytes,
        LinuxHeadingFields.RxPackets,
        LinuxHeadingFields.TxPackets,
        LinuxHeadingFields.BaseAddress
        
    };
    protected static final String[] UnderscoreReplacements = {
        LinuxHeadingFields.LinkEncapUnderscore,
        LinuxHeadingFields.InetAddrUnderscore,
        LinuxHeadingFields.RxBytesUnderscore,
        LinuxHeadingFields.TxBytesUnderscore,
        LinuxHeadingFields.RxPacketsUnderscore,
        LinuxHeadingFields.TxPacketsUnderscore,
        LinuxHeadingFields.BaseAddressUnderscore
        
    };
    
    private String[] SolarisIfconfigHeadings = {
        
        
    };
    
    private String pathDelim = System.getProperty( "file.separator" );
    //private final String command = "C:\\WINDOWS\\system32\\ipconfig.exe";
    private final String netstat_basename = "netstat";
    private String option = "-rn";
    private String command = this.getPathtoExe(netstat_basename);
    private String profile;
    private Vector lines = new Vector();
    private StringBuffer lineBuf = new StringBuffer();
    boolean suspendFlag;
    protected Thread configThread;
    private Hashtable tunnelInfo;
    public Hashtable configItems = new Hashtable();
    public Hashtable remoteRequest = new Hashtable();
    private Hashtable statMapping = new Hashtable();
    private String os = System.getProperty("os.name");
    private Hashtable gateways = new Hashtable();
}

/*
 *
 *Windows IP Configuration
 
        Host Name . . . . . . . . . . . . : amd-1300
        Primary Dns Suffix  . . . . . . . :
        Node Type . . . . . . . . . . . . : Unknown
        IP Routing Enabled. . . . . . . . : No
        WINS Proxy Enabled. . . . . . . . : No
 
Ethernet adapter Local Area Connection:
 
        Connection-specific DNS Suffix  . :
        Description . . . . . . . . . . . : SiS 900 PCI Fast Ethernet Adapter
        Physical Address. . . . . . . . . : 00-D0-09-F5-75-53
        Dhcp Enabled. . . . . . . . . . . : Yes
        Autoconfiguration Enabled . . . . : Yes
        IP Address. . . . . . . . . . . . : 192.168.1.101
        Subnet Mask . . . . . . . . . . . : 255.255.255.0
        Default Gateway . . . . . . . . . : 192.168.1.1
        DHCP Server . . . . . . . . . . . : 192.168.1.1
        DNS Servers . . . . . . . . . . . : 24.221.208.5
                                            24.221.192.5
        Lease Obtained. . . . . . . . . . : Monday, December 15, 2003 11:31:22 AM
        Lease Expires . . . . . . . . . . : Tuesday, December 16, 2003 11:31:22 AM
 
Ethernet adapter Local Area Connection 3:
 
        Connection-specific DNS Suffix  . :
        Description . . . . . . . . . . . : Cisco Systems VPN Adapter
        Physical Address. . . . . . . . . : 00-05-9A-3C-78-00
        Dhcp Enabled. . . . . . . . . . . : Yes
        Autoconfiguration Enabled . . . . : Yes
        Autoconfiguration IP Address. . . : 169.254.18.31
        Subnet Mask . . . . . . . . . . . : 255.255.0.0
        Default Gateway . . . . . . . . . :
 **/
