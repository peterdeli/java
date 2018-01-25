
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;


public class parseLinuxIFConfig extends Thread {
    
    // options
    //
    //  stat [reset] [traffic] [tunnel] [route] [repeat]
    
    public parseLinuxIFConfig(){
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
        System.out.println("End of parseIFconfig(1)" );
    }
    
    public parseLinuxIFConfig(String opt){
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
        System.out.println("End of parseIFConfig (1)" );
    }
    
    
    
    private void printConfig(){
        
        Set keys = interfaces.keySet();
        Iterator it = keys.iterator();
        Hashtable infoHash = null;
        while ( it.hasNext() ){
            Object obj = it.next();
            System.out.println( "Interface: " + obj );
            Object info = interfaces.get( obj );
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
        return interfaces;
    }
    
    
    
    protected String getPathtoExe( String name ){
        String systemPaths = "/usr/bin:/usr/sbin:/sbin:/usr/local/bin";
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
        if ( option.compareTo("-a") == 0 ){
            CmdList.add( option );
        }
        System.out.println( "Command: " + CmdList.toString() );
        String[] CommandArgs = new String[ CmdList.size() ];
        for ( int i=0; i<CommandArgs.length; i++ ){
            CommandArgs[i] = (String)CmdList.elementAt(i);
        }
        
        //String[] cmd_args = { interpreter, interp_opts, command };
        if ( command.length() < 1 ) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), ifconfig_basename + " not found on system path."  );
            return;
        }
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
            InputStreamReader is = new InputStreamReader( in );
            BufferedReader br = new BufferedReader( is );
            OutputStream out = child.getOutputStream();
            //String response =  JOptionPane.showInputDialog( new javax.swing.JFrame(), "Enter Password: " );
            //String password = response.concat("\n");
            int c;
            int count = 0;
            int lastchar = 0;
            
            String currentHeading = "";
            String currentSubHeading = "";
            boolean firstTime = true;
            boolean continueFlag = false;
            String currentLine = "";
            String lastLine;
            boolean loopFlag = true;
            Hashtable items = new Hashtable();
            Vector subHeadingValues = new Vector();
            Hashtable subHeadingItems = new Hashtable();
            //int c;
            //int count = 0;
            // set an alarm if no response within one 30 sec.
            /**
             * eth0      Link encap:Ethernet  HWaddr 00:03:47:B3:CA:0F
             * inet addr:192.168.1.100  Bcast:192.168.1.255  Mask:255.255.255.0
             * UP BROADCAST RUNNING MULTICAST  MTU:1356  Metric:1
             * RX packets:1207059 errors:0 dropped:0 overruns:0 frame:0
             * TX packets:444067 errors:0 dropped:0 overruns:0 carrier:0
             * collisions:0 txqueuelen:100
             * RX bytes:1446299135 (1379.2 Mb)  TX bytes:40803561 (38.9 Mb)
             * Interrupt:11 Base address:0x3000
             **/
            String adapter = null;
            boolean foundAdapterLine = false;
            String line = "";
            boolean interfaceUP = false;
            
            while ( loopFlag == true ) {
                line = br.readLine();
                if ( line == null ) break;
                
                
                if ( line.matches("^[ \t\r\n]*$")) {
                    // new interface
                    // replace spaces in keys which have spaces
                    Hashtable adapterInfo = new Hashtable();
                    Vector tmp = new Vector();
                    Enumeration en = interfaceInfo.elements();
                    String regex = null;
                    String UpIF = ".*" + LinuxHeadingFields.UP + ".*";
                    String BroadcastIF = ".*" + LinuxHeadingFields.BROADCAST + ".*";
                    String RunningIF = ".*" + LinuxHeadingFields.RUNNING + ".*";
                    String MulticastIF = ".*" + LinuxHeadingFields.MULTICAST + ".*";
                    while ( en.hasMoreElements() ){
                        String str = (String)en.nextElement();
                        String newStr = "";
                        for ( int i=0; i<UnderscoreReplacementTargets.length; i++ ){
                            String replaceRegex = ".*" + UnderscoreReplacementTargets[i] + ".*" ;
                            if ( str.matches(replaceRegex ) ){
                                newStr = str.replaceAll( UnderscoreReplacementTargets[i], UnderscoreReplacements[i]);
                                // parse each entry now
                                // get key/value pairs
                                newStr = newStr.trim();
                                regex = ".*" + LinuxHeadingFields.InetAddrUnderscore + ".*";
                                if ( Pattern.matches( regex, newStr )){
                                    
                                    String[] splitLine = newStr.split("[ \t]+");
                                    for ( int j=0; j<splitLine.length; j++ ){
                                        String[] keyval = splitLine[j].split(":", 2);
                                        adapterInfo.put(keyval[0], keyval[1] );
                                    }
                                }
                                
                            }
                            if ( Pattern.matches( UpIF, str ) &&
                            Pattern.matches( BroadcastIF, str ) &&
                            Pattern.matches( RunningIF, str ) &&
                            Pattern.matches( MulticastIF, str )
                            ){
                                interfaceUP = true;
                            }
                        }
                    }
                    
                    if ( interfaceUP ){
                        adapterInfo.put( LinuxHeadingFields.AdapterStateKey,
                        LinuxHeadingFields.UP );
                    } else {
                        adapterInfo.put( LinuxHeadingFields.AdapterStateKey,
                        LinuxHeadingFields.DOWN );
                    }
                    interfaces.put(adapter, adapterInfo );
                    //interfaceInfo.clear();
                    foundAdapterLine = false;
                }
                // look for key
                // first get eth0
                if ( line.startsWith(LinuxHeadingFields.AdapterPrefix) ){
                    String[] adapterLineSplit = line.split(" ", 2 );
                    if ( adapterLineSplit.length > 0 ){
                        adapter = adapterLineSplit[0];
                        configItems.put( LinuxHeadingFields.AdapterKey, adapter );
                        foundAdapterLine = true;
                    }
                }
                if ( foundAdapterLine ){
                    interfaceInfo.addElement(line);
                }
            }
            
            in.close();
            out.close();
            System.out.println( "Ifconfig Parsed" );
            //System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // get Host Name
        
    }
    
    public static void main( String[] args ){
        
        Hashtable hashData = new Hashtable();
        parseIFConfig conf = new parseIFConfig(args[0]);
        System.out.println( "Using IFconfig: " + conf.command );
        System.out.println( "Info ready" );
        hashData = conf.getInfo();
        Enumeration en = hashData.keys();
        while ( en.hasMoreElements() ){
            Object key = en.nextElement();
            System.out.println( key + " = " + hashData.get(key) );
         }
        
        conf.printConfig();
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
    private final String ifconfig_basename = "ifconfig";
    private String command = this.getPathtoExe(ifconfig_basename);
    private String profile;
    private Vector lines = new Vector();
    private StringBuffer lineBuf = new StringBuffer();
    boolean suspendFlag;
    protected Thread configThread;
    private Hashtable tunnelInfo;
    public Hashtable configItems = new Hashtable();
    public Hashtable remoteRequest = new Hashtable();
    private Hashtable statMapping = new Hashtable();
    private String option = "";
    private String os = System.getProperty("os.name");
    private Hashtable interfaces = new Hashtable();
    Vector interfaceInfo = new Vector();
}

