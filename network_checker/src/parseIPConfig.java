

import java.io.*;

import java.util.*;

import java.lang.*;

import java.util.regex.*;

import javax.swing.*;





public class parseIPConfig extends Thread {

    

    // options

    //

    //  stat [reset] [traffic] [tunnel] [route] [repeat]

    

    public parseIPConfig(){

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

        System.out.println("End of parseIPconfig(1)" );

    }

    

    public parseIPConfig(String opt){

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

        System.out.println("End of parseIPConfig (1)" );

    }

    

    

    

    

    

    private void printConfig(){

        

        Set keys = configItems.keySet();

        Object[] keyArray = keys.toArray();

        

        for ( int i=0; i<keyArray.length; i++ ){

            System.out.println( (String)keyArray[i] + ": " + configItems.get(keyArray[i]) );

            

        }

    }

    protected Hashtable getInfo(){

        return configItems;

    }

    

    protected String getPathtoExe( String name ){

        String lib_path = System.getProperty( "java.library.path" );

        

        String validPath = "";

        // split by ;

        String[] paths = lib_path.split(";");

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

        

        

        if ( option.compareTo("/all") == 0 ){

            

            CmdList.add( option );

            

        }

        System.out.println( "Command: " + CmdList.toString() );

        String[] CommandArgs = new String[ CmdList.size() ];

        for ( int i=0; i<CommandArgs.length; i++ ){

            CommandArgs[i] = (String)CmdList.elementAt(i);

        }

        

        //String[] cmd_args = { interpreter, interp_opts, command };

        if ( command.length() < 1 ) {

            JOptionPane.showMessageDialog(new javax.swing.JFrame(), ipconfig_basename + " not found on system path."  );

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

            

            

            

            while ( loopFlag == true ) {

                // get a line

                // continue if null or length < 1

                //boolean inHeadingSection = false;

                //boolean inSubheadingSection = false;

                

                String curLine =br.readLine();

                if ( curLine == null ){

                    break;

                }

                if ( curLine.length() < 1 ) continue;

                

                currentLine = curLine.trim();

                // check if a heading

                continueFlag = false;

                for ( int i=0; i<headings.length; i++ ){

                    

                    // if matches, we'return in a heading section

                    //HeadingFields.WinHeading,

                    //HeadingFields.LanPrefix

                    

                    String regexMatch = new String ( "\"" +  "*" + headings[i] + "*" + "\"");

                    //currentLine.matches( regexMatch )

                    

                    

                    if ( currentLine.startsWith(headings[i]) || currentLine.matches( regexMatch ) ){

                        

                        // if this is our first, don't write previous data

                        //if ( inHeadingSection == false ){

                        //  inHeadingSection = true;

                        if ( i>0 ){

                            // put last subheading

                            subHeadingItems.put(currentSubHeading, subHeadingValues);

                            configItems.put(    currentHeading,  subHeadingItems);

                            subHeadingItems = new Hashtable();

                            subHeadingValues=new Vector();

                            

                        }

                        currentHeading = currentLine;

                        // }

                        continueFlag = true;

                        break;

                    }

                    

                    

                }

                

                // current line was a heading

                if ( continueFlag == true ) continue;

                //

                

                // look for subheadings, get values

                Object[] subHeadings = { winIPKeys, ethernetAdapterKeys };

                boolean breakFlag = false;

                for ( int i=0; i<subHeadings.length; i++ ){

                    String[] subHeading = (String[])subHeadings[i];

                    for ( int j=0; j<subHeading.length; j++ ){

                        String subheading = subHeading[j];

                        if ( currentLine.startsWith(subHeading[j]) ){

                            // start of new subheading section

                            if ( j > 0 ){

                                subHeadingItems.put(currentSubHeading, subHeadingValues);

                                subHeadingValues = new Vector();

                            }

                            currentSubHeading = subHeading[j];

                            // parse key/value out

                            String[] keyVal = currentLine.split(":", 2);

                            subHeadingValues.add(keyVal[1].trim() );

                            breakFlag = true;

                            break;

                        }

                    }

                    // line contains no subheadings

                    // if not null or whitespace, get value

                    if ( breakFlag == true ) break;

                }

                

                if ( breakFlag == false ){

                    String val = currentLine;

                    subHeadingValues.add(  val );

                }

                

            }

            configItems.put(    currentHeading,  subHeadingItems);

            in.close();

            out.close();

            //System.exit(0);

        } catch (Exception e) {

            e.printStackTrace();

        }

        

    }

    

    public static void main( String[] args ){

        

      Hashtable ht = new Hashtable();

        parseIPConfig pi = new parseIPConfig(args[0]);

        System.out.println( "Using IPconfig: " + pi.command );

        System.out.println( "Info ready" );

        ht = pi.getInfo();

        Enumeration headings = ht.keys();

        while ( headings.hasMoreElements() ){

            String heading = (String)headings.nextElement();

            System.out.println( "\nHeading: " + heading );

            Object obj = ht.get(heading);

            if ( obj instanceof Hashtable ){

                Hashtable items = (Hashtable)obj;

                Enumeration subheadings = items.keys();

                while ( subheadings.hasMoreElements() ){

                    String subheading = (String)subheadings.nextElement();

                    System.out.print( "\tSubheading: " + subheading + ": " );

                    Object value = items.get( subheading );

                    if ( value instanceof Vector ){

                        Vector values = (Vector)value;

                        Enumeration en = values.elements();

                        while ( en.hasMoreElements() ) {

                            System.out.println( (String)en.nextElement() );

                        }

                    }

                }

            }

        }

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

        System.out.println( "End of parseIPConfig run()" );

    }

    private String pathDelim = System.getProperty( "file.separator" );

    //private final String command = "C:\\WINDOWS\\system32\\ipconfig.exe";

    private final String ipconfig_basename = "ipconfig.exe";

    private String command = this.getPathtoExe(ipconfig_basename);

    private String profile;

    private Vector lines = new Vector();

    private StringBuffer lineBuf = new StringBuffer();

    

    boolean suspendFlag;

    protected Thread configThread;

    protected static final String[] headings = {

        

        HeadingFields.WinHeading,

        HeadingFields.LanPrefix

        //HeadingFields.LanHeading,

        //HeadingFields.OtherLanHeading

    };

    

    protected static final String[] winIPKeys = {

        HeadingFields.HostName,

        HeadingFields.PrimaryDnsSuffix,

        HeadingFields.NodeType,

        HeadingFields.IPEnabled,

        HeadingFields.WinProxyEnabled

    };

    

    protected static final String[] ethernetAdapterKeys = {

        

        HeadingFields.ConnectionDnsSuffix,

        HeadingFields.Description,

        HeadingFields.PhysicalAddress,

        HeadingFields.DHCPEnabled,

        HeadingFields.AutoConfigEnabled,

        HeadingFields.AutoConfigIPAddress,

        HeadingFields.IPAddress,

        HeadingFields.SubnetMask,

        HeadingFields.DefaultGateway,

        HeadingFields.DHCPServer,

        HeadingFields.DNSServers,

        HeadingFields.PrimaryWinsServer,

        HeadingFields.SecondaryWinsServer,

        HeadingFields.DNSSearchSuffixes,

        HeadingFields.LeaseObtained,

        HeadingFields.LeaseExpires,

        HeadingFields.MediaState

        

    };

    

    

    private Hashtable tunnelInfo;

    public Hashtable configItems = new Hashtable();

    public Hashtable remoteRequest = new Hashtable();

    private Hashtable statMapping = new Hashtable();

    private String option = "";

    

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

