/*
 * ping.java
 *
 * Created on August 25, 2003, 10:59 AM
 */


import java.io.*;
import java.net.*;

/**
 *
 * @author  pdel
 */
public class ping {
    private String hostname = "";
    public final static int ECHO_PORT = 23;
    
    public ping ( String host ){
        hostname = host;
    }
    
    public ping(){
        hostname = "dcs.central.sun.com";
    }
    
    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.out.println("Usage: java ping hostname");
            System.exit(0);
        }
        if (alive(argv[0])) {
            System.out.println(argv[0] + " is alive");
        } else {
            System.out.println("No response from " + argv[0] +". Host is down or does not exist");
        }
    }
    
    public static boolean alive(String host) {
        Socket pingSocket = null;
        try {
            pingSocket = new Socket(host, ECHO_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown Host: " + host );
        } catch (IOException io) {
            System.out.println("IOException: " + io);
        }
        if (pingSocket != null) {
            try {
                pingSocket.close();
            } catch (IOException e) {
                System.err.println("IOException: " + e);
            }
            return true;
        } else {
            return false;
        }
    }
}
