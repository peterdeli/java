/*
 * StatHeadings.java
 *
 * Created on December 8, 2003, 3:57 PM
 */

/**
 *
 * @author  pdel
 */
public interface StatFields
{
    public static final String PACKETS_DISCARDED = "Packets discarded";
    public static final String PACKETS_ENCRYPTED = "Packets encrypted";
    public static final String PACKETS_DECRYPTED = "Packets decrypted";
    public static final String BYTES_IN = "Bytes in";
    public static final String BYTES_OUT = "Bytes out";
    public static final String PACKETS_BYPASSED = "Packets bypassed";
    public static final String TIME_CONNECTED = "Time connected";
    
    public static final String LOCAL_DESTINATION = "Local Network Destination";
    public static final String SECURED_DESTINATION  = "Secured Network Destination";
    public static final String LOCAL_NETMASK = "Local Netmask";
    public static final String SECURED_NETMASK = "Secured Netmask";
    public static final String SERVER_ADDRESS = "Server address";
    public static final String CLIENT_ADDRESS = "Client address";
    public static final String IP_COMPRESSION = "IP Compression";
    public static final String ENCRYPTION = "Encryption";
    public static final String AUTHENTICATION = "Authentication";
    public static final String CONNECTION_ENTRY = "Connection Entry";
    public static final String NAT_PASSTHROUGH = "Local LAN Access";
    public static final String TRANSPARENT_TUNNELING = "NAT passthrough";
    
    
}
/*
VPN traffic summary: 
         {Packets discarded=0, 
         Packets encrypted=56, 
         Bytes in=6529, 
         Packets decrypted=27, 
         Bytes out=9440, 
         Packets bypassed=15, T
         ime connected=0 day(s), 02}
        Configured routes: 
         {Local Network Destination=192.168.1.0, 
         Secured Network Destination=0.0.0.0, L
         ocal Netmask=255.255.255.0, 
         Secured Netmask=0.0.0.0}
        VPN tunnel information: 
         {Server address=203.48.45.210, 
         Client address=129.150.152.2, 
         IP Compression=None, 
         Authentication=HMAC-MD5, 
         Encryption=168-bit 3-DES, 
         Connection Entry=Aus Gateway}
 **/