/*
 * HeadingFields.java
 *
 * Created on December 15; 2003; 11:39 PM
 */

/**
 *
 * @author  Peter Delevoryas
 */
public interface SolarisHeadingFields {
    
    static final String flags = "flags";
    static final String mtu = "mtu";
    static final String index = "index";
    static final String inet = "inet";
    static final String netmask = "netmask";
    static final String broadcast = "broadcast";
    static final String ether = "ether";
    static final String SolarisDefaultGateway = "default";
    static final int SolarisDefaultGatewayCol = 0;
     static final int SolarisDefaultGatewayIPCol = 1;
    static final int SolarisDestinationCol = 1;
    static final int SolarisGatewayCol = 2;
    static final int SolarisFlagsCol = 3;
    static final int SolarisRefCol = 4;
    static final int SolarisUseCol = 5;
    static final int SolarisInterfaceCol = 6;
    static final String SolarisUpGateway = "UG";
    static final String UP = "UP";
    static final String DOWN = "DOWN";
    static final String BROADCAST = "BROADCAST";
    static final String RUNNING = "RUNNING";
    static final String MULTICAST = "MULTICAST";
    static final String AdapterKey = "Adapter";
    static final String AdapterStateKey = "AdapterState";
     static final String GatewayStateKey = "GatewayState";
    static final String hmeAdapter = "hme";
    static final String leAdapter = "le";
    static final String geAdapter = "ge";
    static final String pppAdapter = "ppp";
        
}
