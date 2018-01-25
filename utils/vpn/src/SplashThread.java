/*
 * SplashThread.java
 *
 * Created on November 18, 2003, 12:24 AM
 */

/**
 *
 * @author  pdel
 */
public class SplashThread implements Runnable {
    Thread t;
    vpnSplash vs;
    
    /** Creates a new instance of SplashThread */
    public SplashThread() {
        t=new Thread( this, "Splash Screen Thread" );
        t.start();
        
    }
   
    public void run() {
        try{
             //vs = new vpnSplash(new javax.swing.JFrame(), false);
             
            Thread.sleep(5000);
            System.out.println( "ending thread" );
            vs.hide();
        }catch ( Exception x ){
            
        }
    }
    
    public static void main( String[] args ){
        System.out.println( "new thread" );
        SplashThread st = new SplashThread();
     }

}


