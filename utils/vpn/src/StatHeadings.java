/*
 * StatHeadings.java
 *
 * Created on December 8, 2003, 3:57 PM
 */

/**
 *
 * @author  pdel
 */
public interface StatHeadings {
    public static final String VPN_TRAFFIC = "VPN traffic summary";
    public static final String VPN_ROUTES = "Configured routes";
    public static final String VPN_TUNNEL = "VPN tunnel information";
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