/*
 * comboTest.java
 *
 * Created on December 9, 2003, 1:26 AM
 */

/**
 *
 * @author  pdel
 */

import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.reflect.*;


public class comboTest extends javax.swing.JFrame {
    
    /** Creates new form comboTest */
    public comboTest() {
        initComponents();
        setLookAndFeel();
    }
    protected void setLookAndFeel() {
    
    try {
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel" );
        
        //UIManager.installLookAndFeel("GTK", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" );
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        UIManager.LookAndFeelInfo[] lafInfo = javax.swing.UIManager.getInstalledLookAndFeels();
        
        String propertyName = System.getProperty("os.name");
        
        if (propertyName != null && propertyName.toLowerCase().indexOf("linux") != -1) {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        }
        else {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        }
        
        //UIManager.setInstalledLookAndFeels(lafInfo);
        for ( int i=0; i<lafInfo.length; i++ ){
            System.out.println( "Installed LAF: " + lafInfo[i].getName() );
        }
        System.out.println( "Using: " + UIManager.getLookAndFeel().getDescription() );
    }catch ( Exception x ){
        x.printStackTrace();
    }
}
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "One", "Two", "Three", "Four" }));
        jPanel1.add(jComboBox1);

        jButton1.setText("Press");
        jPanel1.add(jButton1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new comboTest().show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton jButton1;
    protected javax.swing.JComboBox jComboBox1;
    protected javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}