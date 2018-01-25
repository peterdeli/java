import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class ProgressThread extends java.lang.Thread {
    
    vpn3k ui;
    JPanel panel;
    int cols = 10;
    int rows = 1;
    Color bg = Color.blue;
    Vector labels = new Vector();
    boolean go = true;
    long sleeptime = 100;
    private static int instances = 0;
    
    public  ProgressThread(JPanel p)  {
        
        panel=p;
        instances++;
        //clearPanel();
        System.out.println( "Creating ProgressThread" + " go = " + go );
        populatePanel();
        
    }
    public ProgressThread(vpn3k gui, JPanel p) {
        ui=gui;
        panel=p;
        instances++;
        populatePanel();
        
    }
    
    public void stopThread() {
        
        synchronized(this){
            this.go = false;
        }
    }
    
    public void populatePanel() {
        if ( panel.getComponentCount() > 0 ){
            panel.removeAll();
        }
        panel.setLayout(new GridLayout(rows,cols,2,0));
        for ( int i=0; i<cols; i++ ){
            JLabel j = new JLabel();
            panel.add( j, i );
            j.setOpaque(true);
            j.setBackground(bg);
            j.setVisible(false);
            panel.repaint();
        }
    }
    
    public void run() {
         System.out.println( "ProgressThread Running" );
        while ( go ){
           
            try{
                doForward();
                yield();
                hideAll();
                //doBackward();
            } catch ( java.lang.ArrayIndexOutOfBoundsException ob ){
                return;
            }
            
        }
        
        System.out.println( "ProgressThread returning .." );
        instances--;
        return;
    }
    
    public void doForward() {
        for ( int i=0; i<cols; i++ ){
            yield();
            Component c = panel.getComponent(i);
            if ( c instanceof JLabel ){
                JLabel label = (JLabel)c;
                label.setOpaque(true);
                label.setVisible(true);
                panel.repaint();
            }
            try {
                sleep( sleeptime );
                if ( ! go ) {
                    clearPanel();
                    return;
                }
            } catch ( InterruptedException x ){
            }
        }
    }
    
    public void hideAll(){
        for ( int i=0; i<cols; i++ ){
            yield();
            Component c;
            try{
                c = panel.getComponent(i);
            } catch ( java.lang.ArrayIndexOutOfBoundsException ob ){
                return;
            }
            if ( c instanceof JLabel ){
                JLabel label = (JLabel)c;
                label.setVisible(false);
            }
        }
        panel.repaint();
    }
    
    public synchronized void clearPanel(){
        
        
        
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                try {
                    panel.removeAll();
                    panel.repaint();
                } catch ( Exception x ){
                }
            }
        });
        
    }
    
    public void doBackward() {
        int count = panel.getComponentCount();
        
        for ( int i=(count-1); i>=0; i-- ){
            yield();
            
            Component c = panel.getComponent(i);
            if ( c instanceof JLabel ){
                JLabel label = (JLabel)c;
                label.setOpaque(true);
                label.setVisible(false);
                panel.repaint();
            }
            try {
                sleep( sleeptime );
                if ( ! go ) {
                    clearPanel();
                    return;
                }
            } catch ( InterruptedException x ){
            }
        }
    }
    
    public static void main( String[] args ){
        JFrame jf = new JFrame();
        jf.setSize(400,15);
        jf.setUndecorated(true);
        JPanel jp = new JPanel();
        jp.setBackground(Color.green);
        jp.setPreferredSize(new Dimension(400, 15));
        jp.setLayout(new GridLayout(0,10) );
        jf.getContentPane().add( jp );
        jf.setVisible(true);
        int i = 1;
        
            ProgressThread p = new ProgressThread( jp );
            System.out.println( i++ );
            p.start();
            try{
                Thread.currentThread().sleep(5000);
            } catch ( Exception x ){
            }
            p.stopThread();
            
             
        
        
        
        
    }
    
}

