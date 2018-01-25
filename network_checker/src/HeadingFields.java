/*
 * HeadingFields.java
 *
 * Created on October 22, 2004, 4:49 PM
 */

/**
 *
 * @author  pdel
 */
public class HeadingFields {
    
    /** Creates a new instance of HeadingFields */
    public HeadingFields(String ostype) {
        os=ostype;
        populateFields();
    }
    private void populateFields(){
        if ( os.equalsIgnoreCase("solaris" )){
            
        } else if ( os.equalsIgnoreCase("linux")){
            
        } else if ( os.equalsIgnoreCase("windows xp") || os.equalsIgnoreCase("windows 2000" )){
            
        }
        
    }
    
    // Windows Headings
    static final String WinHeading = "Windows IP Configuration";
    static final String HostName = "Host Name";
    static final String PrimaryDnsSuffix ="Primary Dns Suffix";
    static final String NodeType = "Node Type";
    static final String IPEnabled = "IP Routing Enabled";
    static final String WinProxyEnabled = "WINS Proxy Enabled";
    static final String LanPrefix = "Ethernet adapter";
    static final String LanHeading =  "Ethernet adapter Local Area Connection:";
    static final String MediaState = "Media State";
    static final String OtherLanHeading = "Ethernet adapter Local Area Connection";
    static final String ConnectionDnsSuffix =  "Connection-specific DNS Suffix";
    static final String Description = "Description";
    static final String PhysicalAddress =  "Physical Address";
    static final String DHCPEnabled =  "Dhcp Enabled";
    static final String AutoConfigEnabled =  "Autoconfiguration Enabled";
    static final String AutoConfigIPAddress = "Autoconfiguration IP Address";
    static final String IPAddress = "IP Address";
    static final String SubnetMask = "Subnet Mask";
    static final String DefaultGateway = "Default Gateway";
    static final String DHCPServer = "DHCP Server";
    static final String DNSServers = "DNS Servers";
    static final String DNSSearchSuffixes = "DNS Suffix Search List";
    static final String LeaseObtained = "Lease Obtained";
    static final String LeaseExpires = "Lease Expires";
    static final String PrimaryWinsServer = "Primary WINS Server";
    static final String SecondaryWinsServer = "Secondary WINS Server";
    
    //Solaris Headings
    static final String mtu = "mtu";
    static final String index = "index";
    static final String inet = "inet";
    static final String netmask = "netmask";
    static final String broadcast = "broadcast";
    static final String ether = "ether";
    static final int SolarisDestinationCol = 1;
    static final int SolarisGatewayCol = 2;
    static final int SolarisFlagsCol = 3;
    static final int SolarisRefCol = 4;
    static final int SolarisUseCol = 5;
    static final int SolarisInterfaceCol = 6;
    static final String SolarisUpGateway = "UG";
    //UP,BROADCAST,RUNNING,MULTICAST,DHCP
    
    
    // Linux Headings
    static final String LinkEncap = "Link encap";
    static final String LinkEncapUnderscore = "Link_encap";
    static final String HwAddr = "HWaddr";
    static final String InetAddr ="inet addr";
    static final String InetAddrUnderscore = "inet_addr";
    static final String Bcast = "Bcast";
    static final String Mask = "Mask";
    static final String MTU = "MTU";
    static final String UP = "UP";
    static final String DOWN = "DOWN";
    static final String BROADCAST = "BROADCAST";
    static final String RUNNING = "RUNNING";
    static final String MULTICAST = "MULTICAST";
    static final String AdapterStateKey = "AdapterState";
    static final String GatewayStateKey = "GatewayState";
    static final String GatewayInterface = "GatewayInterface";
    static final String Metric = "Metric";
    static final String RxPackets =  "RX packets";
    static final String RxPacketsUnderscore = "RX_packets";
    static final String TxPackets =  "TX packets";
    static final String TxPacketsUnderscore = "TX_packets";
    static final String RxPacketErrors = "errors";
    static final String RxPacketDropped = "dropped";
    static final String RxPacketOverruns = "overruns";
    static final String RxPacketFrame = "frame";
    
    static final String TxPacketErrors = "errors";
    static final String TxPacketDropped = "dropped";
    static final String TxPacketOverruns = "overruns";
    static final String TxPacketCarrier = "carrier";

    static final String Collisions = "collisions";
    static final String TxQueueLen =  "txqueuelen";
    
    static final String RxBytes = "RX bytes";
    static final String RxBytesUnderscore = "RX_bytes";
    static final String TxBytes =  "TX bytes";
    static final String TxBytesUnderscore = "TX_bytes";

    static final String Interrupt =  "Interrupt";
    static final String BaseAddress = "Base address";
    static final String BaseAddressUnderscore = "Base_address";
    static final int LinuxDestCol = 1;
    static final int LinuxGatewayCol = 2;
    static final int LinuxGenmaskCol = 3;
    static final int LinuxFlagsCol = 4;
    static final String LinuxUpGateway = "UG";
    static final int LinuxMssCol = 5;
    static final int LinuxWindowCol = 6;
    static final int LinuxIrttCol = 7;
    static final int LinuxIfaceCol = 8;
    
    static final String AdapterPrefix = "eth";
    static final String AdapterKey = "Adapter";
    
    private String os;
}
