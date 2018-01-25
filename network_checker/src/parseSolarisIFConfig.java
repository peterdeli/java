
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;


public class parseSolarisIFConfig extends Thread {
    
    // options
    //
    //  stat [reset] [traffic] [tunnel] [route] [repeat]
    
    public parseSolarisIFConfig(){
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
    
    public parseSolarisIFConfig(String opt){
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
    
    public Hashtable getInfo(){
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
            //hme0: flags=1004843<UP,BROADCAST,RUNNING,MULTICAST,DHCP,IPv4> mtu 1500 index 2
            //inet 192.168.1.101 netmask ffffff00 broadcast 192.168.1.255
            String interfaceString = "";
            
            while ( loopFlag == true ) {
                line = br.readLine();
                
                if ( line == null ) break;
                line = line.trim();
                
                System.out.println( line );
                
                if ( line.matches( "^[a-z]+[0-9]:.*$" ) &&
                ! line.startsWith("lo0") ){
                    foundAdapterLine = true;
                    // start new interface string
                    // add existing string to vector, then initialize
                    if ( interfaceString.length() > 0 ){
                        adapter = interfaceString.split(": ", 2 )[0];
                        Hashtable adapterInfo = parseInterfaceLine( interfaceString );
                        interfaces.put(adapter, adapterInfo );
                        foundAdapterLine = false;
                        interfaceString = "";
                    }
                    // add to Hash of interfaces
                    interfaceString = interfaceString.concat( " " + line);
                } else {
                    if ( foundAdapterLine ) {
                        interfaceString = interfaceString.concat(" " + line);
                    }
                }
                
            }
            
            in.close();
            out.close();
            
            // do the 'last' interface
            if ( interfaceString.length() > 0 ){
                adapter = interfaceString.split(": ", 2 )[0];
                Hashtable adapterInfo = parseInterfaceLine( interfaceString );
                interfaces.put(adapter, adapterInfo );
                foundAdapterLine = false;
            }
            System.out.println( "Ifconfig Parsed" );
            //System.exit(0);
            
            
        } catch (Exception e) {
            e.printStackTrace();
            
            // get Host Name
        }
    }
    
    public Hashtable parseInterfaceLine( String line ){
        // get IF name by split, then parse string
        String adapter= null;
        boolean interfaceUP = false;
        Hashtable params = new Hashtable();
        String[] adapterLineSplit = line.split(": ", 2 );
        if ( adapterLineSplit.length == 2 ){
            adapter = adapterLineSplit[0];
            String[] adapterParams = adapterLineSplit[1].split(" ");
            //flags=1004843<UP,BROADCAST,RUNNING,MULTICAST,DHCP,IPv4> mtu 1500 index 2
            //inet 192.168.1.101 netmask ffffff00 broadcast 192.168.1.255
            int stopIndex = 9;
            for ( int i=0; i<adapterParams.length; i++ ){
                // flags=
                if ( i==stopIndex ) break;
                switch ( i ){
                    case 0:
                        if ( adapterParams[i].startsWith("flags") ){
                            params.put( SolarisHeadingFields.flags, adapterParams[i]);
                        }
                        break;
                    case 1:
                        if ( adapterParams[i].startsWith(SolarisHeadingFields.mtu) ){
                            params.put( SolarisHeadingFields.mtu, adapterParams[i+1]);
                        }
                        break;
                    case 3:
                        if ( adapterParams[i].startsWith(SolarisHeadingFields.index) ){
                            params.put( SolarisHeadingFields.index, adapterParams[i+1]);
                        }
                        break;
                    case 5:
                        if ( adapterParams[i].startsWith(SolarisHeadingFields.inet) ){
                            params.put( SolarisHeadingFields.inet, adapterParams[i+1]);
                        }
                        break;
                    case 7:
                        if ( adapterParams[i].startsWith(SolarisHeadingFields.netmask) ){
                            params.put( SolarisHeadingFields.netmask, adapterParams[i+1]);
                        }
                        break;
                    case 9:
                        if ( adapterParams[i].startsWith(SolarisHeadingFields.broadcast) ){
                            params.put( SolarisHeadingFields.broadcast, adapterParams[i+1]);
                        }
                        break;
                }
            }
            //flags  0
            // mtu (2) 1
            // index (2) 2
            // inet (2)   3
            // netmask (2) 4
            // broadcast (2) 5
            
        }
        
        String regex = null;
        String UpIF = ".*" + SolarisHeadingFields.UP + ".*";
        String BroadcastIF = ".*" + SolarisHeadingFields.BROADCAST + ".*";
        String RunningIF = ".*" + SolarisHeadingFields.RUNNING + ".*";
        String MulticastIF = ".*" + SolarisHeadingFields.MULTICAST + ".*";
        
        // check for the flags UP,BROADCAST,RUNNING,MULTICAST,DHCP,IPv4>
        // DHCP optional
        if ( Pattern.matches( UpIF, line ) &&
        Pattern.matches( BroadcastIF, line ) &&
        Pattern.matches( RunningIF, line ) &&
        Pattern.matches( MulticastIF, line )
        ){
            params.put( SolarisHeadingFields.AdapterStateKey, "UP" );
        } else {
            params.put( SolarisHeadingFields.AdapterStateKey, "DOWN" );
        }
        
        return params;
    }
    
    public static void main( String[] args ){
        parseSolarisIFConfig conf = null;
        if ( args.length == 1 ){
            conf = new parseSolarisIFConfig(args[0]);
        } else {
            conf = new parseSolarisIFConfig();
        }
        if ( conf == null ) return;
        System.out.println( "Using IFconfig: " + conf.command );
        System.out.println( "Info ready" );
        Hashtable hashData = new Hashtable();
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
        System.out.println( "End of parseSolarisIFConfig run()" );
    }
    
    
    private String[] SolarisIfconfigHeadings = {
        
        
    };
    
    private String pathDelim = System.getProperty( "file.separator" );
    //private final String command = "C:\\WINDOWS\\system32\\ipconfig.exe";
    private final String ifconfig_basename = "ifconfig";
    private String command = this.getPathtoExe(ifconfig_basename);
    private String profile;
    private Vector lines = new Vector();
    private StringBuffer lineBuf = new StringBuffer();
    private boolean suspendFlag;
    protected Thread configThread;
    private Hashtable tunnelInfo;
    public Hashtable configItems = new Hashtable();
    public Hashtable remoteRequest = new Hashtable();
    private Hashtable statMapping = new Hashtable();
    private String option = "-a";
    private String os = System.getProperty("os.name");
    private Hashtable interfaces = new Hashtable();
    Vector interfaceInfo = new Vector();
}

