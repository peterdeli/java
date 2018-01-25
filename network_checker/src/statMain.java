/*
 * statMain.java
 *
 * Created on December 15, 2003, 12:19 PM
 */

/**
 *
 * @author  Peter Delevoryas
 */
public class statMain {
    
    /** Creates a new instance of statMain */
    public statMain() {
        /*
         *# - First, verify that the user is not already connected to the VPN/SWAN
         * ping swan host
         *  # - Computer's network interface physically up?
         *  ipconfig /all - get ethernet interface and default gateway
         *      * check for IP address
         *      * check for default gateway
         *  
         *
         *  # - Local DHCP issue?
            #    how to find out address range being given from router?
            #   Not providing an address to the client?
            #   Correct netmask, subnet address, etc?
         *
         *  # - Connectivity to their default router (DSL/cablemodem)?
         *
         *
         *  # - Connectivity to the Internet?
         *
         *  check vpn servers
         *  # telnet to vpn5k port 80, or ping 3k
         *
         *
         *  # - Are all of the appropriate protocols and ports open?
            #   You will probably need to check their native
            #   IPSec (protocol 50 plus UDP port 500) or
            #   NAT Transparency (TCP 80, 90, 900 or 2020, plus UDP 500).
            #   Of course, the VPN client's settings will need to correlate to the
            # open ports.
         *
         **/
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }
    
}
