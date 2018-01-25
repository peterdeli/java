/*
 * HeadingFields.java
 *
 * Created on December 15; 2003; 11:39 PM
 */

/**
 *
 * @author  Peter Delevoryas
 */
public interface LinuxHeadingFields {
    
    
     
    static final String LinkEncap = "Link encap";
    static final String LinkEncapUnderscore = "Link_encap";
    static final String HwAddr = "HWaddr";
    static final String InetAddr ="inet addr";
    static final String InetAddrUnderscore = "inet_addr";
    static final String Bcast = "Bcast";
    static final String Mask = "Mask";
    static final String MTU = "MTU";
     //
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
    static final int NetstatDestCol = 1;
    static final int NetstatGatewayCol = 2;
    static final int NetstatGenmaskCol = 3;
    static final int NetstatFlagsCol = 4;
    static final String NetstatUpGateway = "UG";
    static final int NetstatMssCol = 5;
    static final int NetstatWindowCol = 6;
    static final int NetstatIrttCol = 7;
    static final int NetstatIfaceCol = 8;
    
    static final String AdapterPrefix = "eth";
    static final String AdapterKey = "Adapter";
    
    // ifconfig fields
    
    
}
