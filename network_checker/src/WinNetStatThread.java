public class WinNetStatThread extends Thread {
    
    private NetStatUI ui;
    
    private Blink LocalHostBlink;
    
    private Blink GatewayLinkBlink;
    
    private Blink GatewayBlink;
    
    private Blink InternetLinkBlink;
    
    private Blink InternetBlink;
    
    private Blink VpnLinkBlink;
    
    private Blink VpnGatewayBlink;
    
    private static final long sleepInterval = 2000;
    
    protected parseIPConfig IPConfig;
    
    protected parseIFConfig IFConfig;
    
    protected Hashtable ConfigInfo;
    
    protected Object LanInfoObject;
    
    Hashtable LanInfo;
    
    protected final String DefaultVpnServer = "ivpn-central.sun.com";
    
    protected final int DefaultVpnPort = 80;
    
    protected boolean validHost = false;
    
    protected final String SwanHost = "sunweb.central.sun.com";
    
    public WinNetStatThread(NetStatUI UI) {
        ui=UI;
        LocalHostBlink = new Blink( ui.LocalHostButton );
        GatewayLinkBlink = new Blink( ui.LocalToGatewayLink );
        GatewayBlink = new Blink( ui.GatewayButon );
        InternetLinkBlink = new Blink( ui.GatewayToInternetLink );
        InternetBlink = new Blink( ui.InternetButton );
        VpnLinkBlink = new Blink( ui.InternetToVpnLink );
        VpnGatewayBlink = new Blink( ui.VpnGatewayButton );
        
        //start();
    }
    
    public String getHostName(Hashtable configInfo) {
        Enumeration enum = configInfo.keys();
        Hashtable hostnames = new Hashtable();
        String hostname = "";
        while ( enum.hasMoreElements() ){
            String key = enum.nextElement().toString();
            if ( key.startsWith( WinHeadingFields.WinHeading ) ){
                Object val = configInfo.get( key );
                if ( val instanceof Hashtable ){
                    Hashtable h = (Hashtable)val;
                    Object subval = h.get( WinHeadingFields.HostName );
                    if ( subval instanceof Vector){
                        Vector v = (Vector)subval;
                        hostname = (String)v.get(0);
                    }
                }
            }
        }
        if ( hostname.length() < 1 ) hostname = "LocalHost";
        return hostname;
    }
    
    public Hashtable getAdapters(Hashtable configInfo) {
        Enumeration enum = configInfo.keys();
        Hashtable adapters = new Hashtable();
        
        while ( enum.hasMoreElements() ){
            String key = enum.nextElement().toString();
            if ( key.startsWith(HeadingFields.LanPrefix) ){
                Object val = configInfo.get( key );
                if ( val instanceof Hashtable ){
                    adapters.put(key, val);
                }
            }
        }
        return adapters;
    }
    
    public boolean hasDefaultGateway(Hashtable adapter) {
        // key points to hashtable of info
        if ( adapter.size() < 1 ){
            return false;
        }
        // get hashtable
        if ( adapter.containsKey(HeadingFields.DefaultGateway ) ){
            Vector gw = (Vector)adapter.get( HeadingFields.DefaultGateway );
            if ( gw.size() < 1 || gw == null ) return false;
            return true;
        }
        return false;
    }
    
    public void getConfig() {
        
        // based on OS
        if ( this.ui.OSType.startsWith("windows") ){
            IPConfig = new parseIPConfig("/all");
            ConfigInfo = IPConfig.getInfo();
            // an 'up' interface will have an IP # and a gateway
            // get IP address and gateway
            // get Adapters
            LanInfoObject = ConfigInfo.get( WinHeadingFields.LanHeading );
            // contains Hashtable pointing to vector of items
            LanInfo = new Hashtable();
            if ( LanInfoObject instanceof Hashtable ){
                LanInfo = (Hashtable)LanInfoObject;
                if ( LanInfo.size() < 1 ){
                    JOptionPane.showMessageDialog(new JFrame(), "No information from IPConfig", "IP Config Error", JOptionPane.ERROR_MESSAGE );
                    ui.Check.setEnabled(false);
                    ui.Cancel.setEnabled(false);
                }
            }
        } else if ( this.ui.OSType.equalsIgnoreCase("linux" ) ){
            
        } else if (  this.ui.OSType.equalsIgnoreCase( "solaris" )){
            
        } else {
            System.out.println( "Cannot work with Operating System " + this.ui.OSType );
            return;
        }
        
        
    }
    
    public void run() {
        
        // check whether UI cancel button has been pressed //
        System.out.println( "Checking connection .." );
        ui.printToLog("Checking connection .." );
        
        getConfig();
        if ( stopThread() )  return;
        
        //NetStat stat = new NetStat();
        ui.LocalHostButton.setText( this.getHostName(ConfigInfo) );
        ui.LocalHostButton.setVisible(true);
        LocalHostBlink.start();
        
        try{
            sleep( sleepInterval );
            yield();
        } catch ( InterruptedException iex ){
            
        }
        
        //- First, verify that the user is not already connected to the VPN/SWAN
        
        
        if ( this.checkSwan() == true ){
            if ( stopThread() )  return;
            ui.LocalHostButton.setBackground(Color.green);
            ui.LocalHostButton.setText(this.getHostName(ConfigInfo));
            ui.LocalToGatewayLink.setBackground(Color.green);
            ui.GatewayButon.setBackground(Color.green);
            ui.GatewayToInternetLink.setBackground(Color.green);
            ui.InternetButton.setBackground(Color.green);
            ui.InternetToVpnLink.setBackground(Color.green);
            ui.VpnGatewayButton.setBackground(Color.green);
            System.out.println( "Connection to SWAN OK" );
            ui.printToLog("Connection to SWAN OK" );
            LocalHostBlink.stopBlinking();
            
            ui.hideNetworkObjects();
            ui.LocalHostButton.setVisible(true);
            ui.LocalToGatewayLink.setVisible(true);
            ui.SwanButton.setBackground(Color.green);
            ui.displayPanel.add(ui.SwanButton, 2);
            ui.SwanButton.setVisible(true);
            ui.SwanButton.setText(this.SwanHost);
            ui.displayPanel.repaint();
            
        }  else {
            if ( stopThread() )  return;
            System.out.println( "No Connection to SWAN, checking network configuration .." );
            ui.printToLog("No Connection to SWAN, checking network configuration .." );
            LocalHostBlink.stopBlinking();
            ui.LocalToGatewayLink.setVisible(true);
            GatewayLinkBlink.start();
            try{
                
                sleep( sleepInterval );
                yield();
            } catch ( InterruptedException iex ){
                
            }
            
            if ( stopThread() )  return;
            
            Hashtable adapters = getAdapters(ConfigInfo);
            if ( adapters.size() < 1 ){
                String msg = "No Network Interfaces found";
                System.out.println(msg);
                ui.printToLog(msg);
                GatewayLinkBlink.stopBlinking();
                setColors(Color.red);
                resetButtons();
                return;
            }
            
            // check for gateways
            Enumeration adapterEnum = adapters.keys();
            Hashtable gatewayAdapters = new Hashtable();
            while ( adapterEnum.hasMoreElements() ){
                String key = (String)adapterEnum.nextElement();
                Hashtable adapterInfo = (Hashtable)adapters.get(key);
                if ( hasDefaultGateway(adapterInfo) ){
                    gatewayAdapters.put(key, adapterInfo);
                }
            }
            
            if ( gatewayAdapters.size() < 1 ){
                ui.LocalToGatewayLink.setVisible(true);
                
                ui.LocalToGatewayLink.setBackground(Color.red);
                ui.GatewayButon.setVisible(true);
                ui.GatewayButon.setBackground(Color.red);
                String msg = new String( "No Default Gateways found!\n" +  "Check connection to Default Gateway" );
                System.out.println(msg);
                ui.printToLog(msg);
                resetButtons();
                return;
            }
                /*
                //"Media State . . . . . . . . . . . : Media disconnected" ??
                if ( LanInfo.containsKey( HeadingFields.MediaState ) ){
                    Vector MediaState = (Vector)LanInfo.get( HeadingFields.MediaState );
                    String state = MediaState.get(0).toString().trim();
                    if ( state.startsWith("Media disconnected") ){
                        System.out.println( "Network cable unplugged, check connections" );
                        GatewayLinkBlink.stopBlinking();
                        setColors(Color.red);
                        ui.printToLog("Network cable unplugged, check connections" );
                        return;
                    }
                }
                 **/
            
            // cycle through adapters
            adapterEnum =  gatewayAdapters.keys();
            
            while ( adapterEnum.hasMoreElements() ){
                
                if ( stopThread() )  return;
                
                String adapter = (String)adapterEnum.nextElement();
                System.out.println( "Adapter: " + adapter );
                Hashtable adapterInfo = (Hashtable)gatewayAdapters.get( adapter );
                
                Vector IPAddress = (Vector)adapterInfo.get( WinHeadingFields.IPAddress );
                Vector DefaultGW = (Vector)adapterInfo.get( WinHeadingFields.DefaultGateway );
                Vector NetMask = (Vector)adapterInfo.get( WinHeadingFields.SubnetMask );
                Vector DnsServers = (Vector)adapterInfo.get( WinHeadingFields.DNSServers );
                
                String msg = new String(
                "\n" + "IP address: " + (String)IPAddress.get(0) + "\n" +
                "Default Gateway: " + (String)DefaultGW.get(0)  + "\n" +
                "Netmask: " + (String)NetMask.get(0) + "\n" );
                
                System.out.println( msg );
                ui.printToLog(msg);
                
                String DefaultGateway = (String)DefaultGW.get(0);
                //- Connectivity to their default router (DSL/cablemodem)?
                
                if ( this.checkGateway(DefaultGateway ) ){
                    
                    GatewayLinkBlink.stopBlinking();
                    ui.LocalToGatewayLink.setBackground(Color.green);
                    ui.GatewayButon.setVisible(true);
                    ui.GatewayButon.setBackground(Color.green);
                    
                    msg = ( "Connection to Default Gateway " + DefaultGateway + " OK" );
                    System.out.println( msg );
                    ui.printToLog(msg);
                    
                    // Check for host on network
                    ui.GatewayToInternetLink.setVisible(true);
                    InternetLinkBlink.start();
                    
                    try{
                        sleep( sleepInterval );
                        yield();
                    } catch ( InterruptedException iex ){
                        
                    }
                    
                    
                    if ( stopThread() )  return;
                    
                    msg = ( "Checking Connection to Internet"  );
                    System.out.println( msg );
                    ui.printToLog(msg);
                    
                    if ( this.checkInternet() ){
                        msg = ( "Connection to Internet OK"  );
                        
                        
                        System.out.println( msg );
                        ui.printToLog(msg);
                        InternetLinkBlink.stopBlinking();
                        ui.GatewayToInternetLink.setBackground(Color.green);
                        ui.InternetButton.setVisible(true);
                        ui.InternetButton.setBackground(Color.green);
                        ui.displayPanel.repaint();
                    } else {
                        
                        msg = ( "No Connection to Internet"  );
                        System.out.println( msg );
                        ui.printToLog(msg);
                        InternetLinkBlink.stopBlinking();
                        ui.GatewayToInternetLink.setBackground(Color.red);
                        ui.InternetButton.setVisible(true);
                        ui.InternetButton.setBackground(Color.red);
                        ui.displayPanel.repaint();
                        resetButtons();
                        return;
                    }
                    
                /*
                System.out.println( "Checking ISP connection .. pinging DNS server" );
                if ( stat.Host.isAlive( (String)DnsServers.get(0) ) ){
                    System.out.println( "Ping of ISP DNS server: " + (String)DnsServers.get(0) + " is alive" );
                 
                } else {
                    System.out.println( "Ping of ISP DNS server: " + (String)DnsServers.get(0) + " not responding or host not found" );
                }
                 **/
                    
                    if ( stopThread() )  return;
                    
                    
                    ui.InternetToVpnLink.setVisible(true);
                    VpnLinkBlink.start();
                    //VpnGatewayBlink
                    
                    msg = ( "Checking Connection to VPN server " );
                    System.out.println( msg );
                    ui.printToLog(msg);
                    boolean connectOk = false;
                    if ( checkVpn() == true ){
                        
                        VpnLinkBlink.stopBlinking();
                        ui.InternetToVpnLink.setBackground(Color.green);
                        ui.VpnGatewayButton.setVisible(true);
                        ui.VpnGatewayButton.setBackground(Color.green);
                        ui.displayPanel.repaint();
                        msg = ( "Connectivity to VPN server OK" );
                        System.out.println( msg );
                        ui.printToLog(msg);
                        connectOk = true;
                        if ( stopThread() )  return;
                    } else {
                        msg = ( "No connection to VPN Server"   );
                        System.out.println(msg);
                        ui.printToLog(msg);
                        ui.InternetToVpnLink.setBackground(Color.red);
                        ui.VpnGatewayButton.setVisible(true);
                        ui.VpnGatewayButton.setBackground(Color.red);
                        if ( stopThread() )  return;
                    }
                    
                    
                        /*
                        try {
                            Socket vpnSocket = new Socket( DefaultVpnServer, DefaultVpnPort );
                         
                            try{
                                sleep( sleepInterval );
                                yield();
                            } catch ( InterruptedException iex ){
                         
                            }
                            if ( stopThread() )  return;
                            VpnLinkBlink.stopBlinking();
                            ui.InternetToVpnLink.setBackground(Color.green);
                            ui.VpnGatewayButton.setVisible(true);
                            ui.VpnGatewayButton.setBackground(Color.green);
                            ui.displayPanel.repaint();
                            msg = ( "Connectivity to " + DefaultVpnServer + " OK" );
                            System.out.println( msg );
                            ui.printToLog(msg);
                            connectOk = true;
                            vpnSocket.close();
                        } catch ( UnknownHostException uh  ){
                         
                            // 0, 1, 2
                            // 0 -> successful ( gateway name )
                            // 1 -> unknown host
                            // 2 -> IO exception
                         
                            msg = ( "No connection to " + DefaultVpnServer + ": Host Unknown" );
                            System.out.println(msg);
                            ui.printToLog(msg);
                            ui.InternetToVpnLink.setBackground(Color.red);
                            ui.VpnGatewayButton.setVisible(true);
                            ui.VpnGatewayButton.setBackground(Color.red);
                            //uh.printStackTrace();
                            System.out.println( uh.getLocalizedMessage() );
                        } catch ( IOException iox ){
                            if ( connectOk == false ){
                                msg = ( "No connection to " + DefaultVpnServer  + ": IO Exception" );
                                System.out.println(msg);
                                System.out.println( iox.getLocalizedMessage() );
                                ui.InternetToVpnLink.setBackground(Color.red);
                                ui.VpnGatewayButton.setVisible(true);
                                ui.VpnGatewayButton.setBackground(Color.red);
                                //iox.printStackTrace();
                            }
                        }
                         **/
                    
                } else {
                    
                    msg = ( "No Connection to Default Gateway " + DefaultGateway + "\n" +
                    "Check connection to Default Gateway. " +  "Netmask: " + (String)NetMask.get(0) );
                    System.out.println(msg);
                    ui.printToLog(msg);
                    if ( adapterEnum.hasMoreElements() ){
                        continue;
                    } else {
                        ui.LocalToGatewayLink.setVisible(true);
                        ui.LocalToGatewayLink.setBackground(Color.red);
                        ui.GatewayButon.setVisible(true);
                        ui.GatewayButon.setBackground(Color.red);
                        //ui.LocalToGatewayLink.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gray_arrow_red_x.gif")));
                        
                        //return;
                    }
                }
                
                
                // if more adapters here, pop up dialog to continue
                if ( adapterEnum.hasMoreElements() ){
                    msg = "Click on 'Yes' to check the next adapter, or 'No' to cancel checking";
                    int response = JOptionPane.showConfirmDialog(new JFrame(), msg, "Check Next Adapter?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
                    if ( response == JOptionPane.NO_OPTION ){
                        break;
                    } else {
                        // hide all objects and stop blinking
                        LocalHostBlink.stopBlinking();
                        GatewayBlink.stopBlinking();
                        GatewayLinkBlink.stopBlinking();
                        InternetBlink.stopBlinking();
                        InternetLinkBlink.stopBlinking();
                        VpnGatewayBlink.stopBlinking();
                        VpnLinkBlink.stopBlinking();
                        
                        setColors( Color.lightGray );
                        LocalHostButton.setBackground(Color.green);
                        hideNetworkObjects();
                        continue;
                    }
                }
            }
        }
        resetButtons();
        
    }
    
    private void resetButtons() {
        ui.Cancel.setEnabled(false);
        ui.Check.setEnabled(true);
    }
    
    private boolean stopThread() {
        boolean status = false;
        if ( ui.CancelNetStat == true ){
            ui.NetStatStopped = true;
            ui.Check.setEnabled(true);
            ui.Cancel.setEnabled(false);
            ui.hideNetworkObjects();
            ui.printToLog("Network check cancelled.\n" );
            status = true;
        }
        return status;
    }
    
    private boolean checkInternet() {
        // ping or socket connect to hosts
        //boolean canConnect = false;
        String msg = "";
        int successCount = 0;
        
        int maxRetries = 50;
        long sleepInterval = 10000 / maxRetries;
        
        final String[] InternetHosts =  { "http://www.yahoo.com", "http://www.sun.com", "http://www.ebay.com", "http://www.java.com" };
        
        for ( int i=0; i<InternetHosts.length; i++ ){
            final String currentHost = InternetHosts[i];
            
            // set timer for each connection attempt
            class URLThread extends Thread{
                NetStatUI.NetStatThread netstatT;
                public URLThread( NetStatUI.NetStatThread th ){
                    netstatT = th;
                }
                
                public void run(){
                    
                    try{
                        URL InternetURL = new URL( currentHost );
                        InternetURL.openStream();
                        System.out.println( "Connection to " + currentHost + " OK." );
                        netstatT.validHost = true;
                        return;
                        
                    } catch ( MalformedURLException mue ){
                        netstatT.validHost = false;
                        return;
                    }catch ( IOException ie ){
                        netstatT.validHost = false;
                        return;
                    }
                    
                    //System.out.println( "End of URLThread.run(), Host " + currentHost + " is validHost == " + validHost );
                    
                }
                public boolean isValidHost(){
                    return netstatT.validHost;
                }
            }
            
            URLThread urlt = new URLThread(this);
            urlt.start();
            
            for ( int j=0; j<maxRetries; j++ ){
                try{
                    yield();
                    sleep( sleepInterval );
                }catch ( java.lang.InterruptedException x ){
                }
                if (  this.validHost ){
                    
                    msg = new String( "HTTP connectivity to " + InternetHosts[i] +  " OK" );
                    printToLog(msg);
                    //canConnect = true;
                    successCount++;
                    urlt = null;
                    break;
                }
                
                //URL InternetURL = new URL( InternetHosts[i] );
                //InternetURL.openStream();
            }
            urlt = null;
            if (  this.validHost == false ){
                msg = new String( "No HTTP connectivity to " + InternetHosts[i]  );
                printToLog(msg);
            }
            this.validHost = false;
        }
        if ( successCount > 0 ) return true;
        return false;
        
    }
    
    private boolean checkVpn() {
        // socket 80 vpn gateways
        String[] vpnServers = { "ivpn-central.sun.com", "ivpn-sfbay.sun.com", "ivpn-east.sun.com",
        "ivpn-aus.sun.com", "ivpn-holland.sun.com", "ivpn-japan.sun.com", "ivpn-singapore.sun.com",
        "ivpn-prc.sun.com", "ivpn-norway.sun.com", "ivpn-uk.sun.com", "bongo.sun.com" };
        Object response = JOptionPane.showInputDialog(new JFrame(), "Select a vpn server to check:", "Select a vpn server", JOptionPane.QUESTION_MESSAGE, null, vpnServers, "ivpn-central.sun.com" );
        String VpnServer = "";
        String msg = "";
        if ( response instanceof String ){
            VpnServer = (String)response;
        } else {
            // cancel check
            ui.cancelNetStat(null);
            return false;
            //VpnServer = "ivpn-central.sun.com";
        }
        int DefaultVpnPort = 80;
        try {
            Socket VpnSocket = new Socket( VpnServer, DefaultVpnPort );
            msg = new String( "Connection to " + VpnServer + " at port " + DefaultVpnPort + " OK\n" );
            printToLog(msg);
        } catch ( UnknownHostException uh ){
            
            msg = new String( "No Connection to " + VpnServer + " at port " + DefaultVpnPort + "\n" );
            
            printToLog(msg);
            
            return false;
        } catch ( IOException io ){
            String errorMsg = io.getMessage();
            msg = new String( "No Connection to " + VpnServer + " at port " + DefaultVpnPort + "\n" );
            
            printToLog(msg);
            return false;
        }
        return true;
    }
    
    private boolean checkSwan() {
        
        // ping set of machines
        // url connect to sunweb.<domain>
        WinPing swanHost = new WinPing();
        return swanHost.isAlive(this.SwanHost);
        
    }
    
    private boolean checkGateway(String gw) {
        WinPing host = new WinPing();
        if ( host.isAlive(gw) )return true;
        return false;
    }
    
    private boolean checkLocalHost() {
        
        return true;
        
    }
    
    private boolean checkConnection(String type) {
        return true;
    }
    
}

