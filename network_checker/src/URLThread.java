/*
 * URLThread.java
 *
 * Created on November 3, 2004, 12:56 PM
 */

/**
 *
 * @author  pdel
 */
import java.net.*;

public class URLThread extends Thread {
    
           
            
                    NetStatUI.NetStatThread netstatT;
                    public URLThread( NetStatUI.NetStatThread th ){
                        netstatT = th;
                    }
                    
                    public void run(){
                        
                        try{
                            java.net.URL InternetURL = new java.net.URL( currentHost );
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
