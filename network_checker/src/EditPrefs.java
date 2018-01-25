/*
 * EditPrefs.java
 *
 * Created on November 22, 2004, 12:37 PM
 */

/**
 *
 * @author  pdel
 */

import java.io.*;
import java.util.*;

public class EditPrefs extends javax.swing.JFrame {
    
    private NetStatUI ui;
    private String prefsFileName;
    int maxIndex = 3;
    private Vector SwanHostList = new Vector();
    private Vector InternetHostList = new Vector();
    private String SwanKey = "SwanHosts";
    private String InternetKey = "InternetHosts";
    private String delimiter = ",";
    
    
    /** Creates new form EditPrefs */
    public EditPrefs() {
        setVisible(false);
        initComponents();
        populateFields();
    }
    
    public EditPrefs(NetStatUI UI) {
        setVisible(false);
        ui=UI;
        prefsFileName = ui.getPrefsFile();
        initComponents();
        this.setIconImage( ui.getMinimizeImage() );
        populateFields();
        ui.centerWindow(this);
    }
    
    private void populateFields(){
        // read in prefs file if found
        try {
            File prefsFile = new File( prefsFileName );
            if ( prefsFile.exists() ){
                String line;
                try{
                    BufferedReader br = new BufferedReader( new FileReader( prefsFile));
                    while ( (line = br.readLine()) != null ){
                        String[] splitLine = line.split(" ", 2);
                        if ( splitLine.length == 2 ){
                            String type = splitLine[0];
                            String[] hosts = splitLine[1].split(delimiter);
                            if ( hosts.length > 0 ){
                                for ( int i=0; i<hosts.length; i++ ){
                                    if ( i == maxIndex ) break;
                                    
                                    switch (i) {
                                        case 0:
                                            if ( type.equals(SwanKey) ){
                                                SwanHost1Field.setText(hosts[i]);
                                                if ( hosts[i].length() > 0 )
                                                SwanHostList.add( hosts[i]);
                                            }
                                            if ( type.equals(InternetKey) ){
                                                InternetHost1Field.setText(hosts[i]);
                                                if ( hosts[i].length() > 0 )
                                                InternetHostList.add(  hosts[i] );
                                            }
                                            break;
                                        case 1:
                                            if ( type.equals(SwanKey) ){
                                                SwanHost2Field.setText(hosts[i]);
                                                if ( hosts[i].length() > 0 )
                                                SwanHostList.add(hosts[i]);
                                            }
                                            if ( type.equals(InternetKey) ){
                                                InternetHost2Field.setText(hosts[i]);
                                                if ( hosts[i].length() > 0 )
                                                InternetHostList.add(hosts[i] );
                                            }
                                            break;
                                        case 2:
                                            if ( type.equals(SwanKey) ){
                                                SwanHost3Field.setText(hosts[i]);
                                                 if ( hosts[i].length() > 0 )
                                                SwanHostList.add( hosts[i]);
                                            }
                                            if ( type.equals(InternetKey) ){
                                                InternetHost3Field.setText(hosts[i]);
                                                if ( hosts[i].length() > 0 )
                                                InternetHostList.add( hosts[i] );
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    br.close();
                } catch ( java.io.FileNotFoundException fnf ){
                    
                } catch ( java.io.IOException ioe ){
                    
                }
                
            } else {
                // otherwise read in defaults
                String swanHosts = ui.getSwanHosts();
                String[] swanList = swanHosts.split(delimiter);
                
                String internetHosts = ui.getInternetHosts();
                String[] internetList = internetHosts.split(delimiter);
                
                
                for ( int i=0; i<swanList.length; i++ ){
                    if ( i == maxIndex ) break;
                    switch (i) {
                        case 0:SwanHost1Field.setText(swanList[i]);
                        SwanHostList.add(i, swanList[i]);
                        break;
                        case 1:SwanHost2Field.setText(swanList[i]);
                        SwanHostList.add(i, swanList[i]);
                        break;
                        case 2: SwanHost3Field.setText(swanList[i]);
                        SwanHostList.add(i, swanList[i]);
                        break;
                    }
                }
                for ( int i=0; i<internetList.length; i++ ){
                    if ( i == maxIndex ) break;
                    switch (i) {
                        case 0:InternetHost1Field.setText(internetList[i]);
                        InternetHostList.add( i, internetList[i] );
                        break;
                        case 1:InternetHost2Field.setText(internetList[i]);
                        InternetHostList.add( i, internetList[i] );
                        break;
                        case 2: InternetHost3Field.setText(internetList[i]);
                        InternetHostList.add( i, internetList[i] );
                        break;
                    }
                }
                
                
            }
            
        } catch ( java.lang.NullPointerException np ){
            return;
        }
        // populate fields
        
        ui.setSwanHosts(SwanHostList);
        ui.setInternetHosts(InternetHostList);
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        SwanHost1Label = new javax.swing.JLabel();
        SwanHost1Field = new javax.swing.JTextField();
        SwanHost2Label = new javax.swing.JLabel();
        SwanHost2Field = new javax.swing.JTextField();
        SwanHost3Label = new javax.swing.JLabel();
        SwanHost3Field = new javax.swing.JTextField();
        InternetHost1Label = new javax.swing.JLabel();
        InternetHost1Field = new javax.swing.JTextField();
        InternetHost2Label = new javax.swing.JLabel();
        InternetHost2Field = new javax.swing.JTextField();
        InternetHost3Label = new javax.swing.JLabel();
        InternetHost3Field = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        Save = new javax.swing.JButton();
        Restore = new javax.swing.JButton();
        Help = new javax.swing.JButton();
        Close = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setTitle("NetCheck Preferences");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel1.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Desktop.background"));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Host Settings");
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        jPanel2.setMaximumSize(new java.awt.Dimension(350, 175));
        jPanel2.setMinimumSize(new java.awt.Dimension(350, 175));
        jPanel2.setPreferredSize(new java.awt.Dimension(350, 175));
        SwanHost1Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        SwanHost1Label.setText("Swan Host #1");
        SwanHost1Label.setMaximumSize(new java.awt.Dimension(110, 25));
        SwanHost1Label.setMinimumSize(new java.awt.Dimension(110, 25));
        SwanHost1Label.setPreferredSize(new java.awt.Dimension(110, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel2.add(SwanHost1Label, gridBagConstraints);

        SwanHost1Field.setToolTipText("First host to be checked for Swan connectivity");
        SwanHost1Field.setMaximumSize(new java.awt.Dimension(200, 25));
        SwanHost1Field.setMinimumSize(new java.awt.Dimension(200, 25));
        SwanHost1Field.setPreferredSize(new java.awt.Dimension(200, 25));
        jPanel2.add(SwanHost1Field, new java.awt.GridBagConstraints());

        SwanHost2Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        SwanHost2Label.setText("Swan Host #2");
        SwanHost2Label.setMaximumSize(new java.awt.Dimension(110, 25));
        SwanHost2Label.setMinimumSize(new java.awt.Dimension(110, 25));
        SwanHost2Label.setPreferredSize(new java.awt.Dimension(110, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel2.add(SwanHost2Label, gridBagConstraints);

        SwanHost2Field.setToolTipText("Second host to be checked for Swan connectivity");
        SwanHost2Field.setMaximumSize(new java.awt.Dimension(200, 25));
        SwanHost2Field.setMinimumSize(new java.awt.Dimension(200, 25));
        SwanHost2Field.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel2.add(SwanHost2Field, gridBagConstraints);

        SwanHost3Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        SwanHost3Label.setText("Swan Host #3");
        SwanHost3Label.setMaximumSize(new java.awt.Dimension(110, 25));
        SwanHost3Label.setMinimumSize(new java.awt.Dimension(110, 25));
        SwanHost3Label.setPreferredSize(new java.awt.Dimension(110, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel2.add(SwanHost3Label, gridBagConstraints);

        SwanHost3Field.setToolTipText("Third host to be checked for Swan connectivity");
        SwanHost3Field.setMaximumSize(new java.awt.Dimension(200, 25));
        SwanHost3Field.setMinimumSize(new java.awt.Dimension(200, 25));
        SwanHost3Field.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel2.add(SwanHost3Field, gridBagConstraints);

        InternetHost1Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        InternetHost1Label.setText("Internet Host #1");
        InternetHost1Label.setMaximumSize(new java.awt.Dimension(110, 25));
        InternetHost1Label.setMinimumSize(new java.awt.Dimension(110, 25));
        InternetHost1Label.setPreferredSize(new java.awt.Dimension(110, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel2.add(InternetHost1Label, gridBagConstraints);

        InternetHost1Field.setToolTipText("First host to check for internet connectivity");
        InternetHost1Field.setMaximumSize(new java.awt.Dimension(200, 25));
        InternetHost1Field.setMinimumSize(new java.awt.Dimension(200, 25));
        InternetHost1Field.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel2.add(InternetHost1Field, gridBagConstraints);

        InternetHost2Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        InternetHost2Label.setText("Internet Host #2");
        InternetHost2Label.setMaximumSize(new java.awt.Dimension(110, 25));
        InternetHost2Label.setMinimumSize(new java.awt.Dimension(110, 25));
        InternetHost2Label.setPreferredSize(new java.awt.Dimension(110, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel2.add(InternetHost2Label, gridBagConstraints);

        InternetHost2Field.setToolTipText("Second host to check for internet connectivity");
        InternetHost2Field.setMaximumSize(new java.awt.Dimension(200, 25));
        InternetHost2Field.setMinimumSize(new java.awt.Dimension(200, 25));
        InternetHost2Field.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        jPanel2.add(InternetHost2Field, gridBagConstraints);

        InternetHost3Label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        InternetHost3Label.setText("Internet Host #3");
        InternetHost3Label.setMaximumSize(new java.awt.Dimension(110, 25));
        InternetHost3Label.setMinimumSize(new java.awt.Dimension(110, 25));
        InternetHost3Label.setPreferredSize(new java.awt.Dimension(110, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel2.add(InternetHost3Label, gridBagConstraints);

        InternetHost3Field.setToolTipText("Third host to check for internet connectivity");
        InternetHost3Field.setMaximumSize(new java.awt.Dimension(200, 25));
        InternetHost3Field.setMinimumSize(new java.awt.Dimension(200, 25));
        InternetHost3Field.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        jPanel2.add(InternetHost3Field, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Desktop.background"));
        Save.setMnemonic('s');
        Save.setText("Save");
        Save.setToolTipText("Save Settings");
        Save.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Save.setPreferredSize(new java.awt.Dimension(50, 25));
        Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Save(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(Save, gridBagConstraints);

        Restore.setMnemonic('r');
        Restore.setText("Restore Defaults");
        Restore.setToolTipText("Restore original host defaults");
        Restore.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Restore.setMaximumSize(new java.awt.Dimension(125, 25));
        Restore.setMinimumSize(new java.awt.Dimension(125, 25));
        Restore.setPreferredSize(new java.awt.Dimension(125, 25));
        Restore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restoreDefaults(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel3.add(Restore, gridBagConstraints);

        Help.setMnemonic('h');
        Help.setText("Help");
        Help.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Help.setMaximumSize(new java.awt.Dimension(70, 25));
        Help.setMinimumSize(new java.awt.Dimension(70, 25));
        Help.setPreferredSize(new java.awt.Dimension(70, 25));
        Help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Help(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(Help, gridBagConstraints);

        Close.setMnemonic('c');
        Close.setText("Close");
        Close.setToolTipText("Close");
        Close.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Close.setMaximumSize(new java.awt.Dimension(40, 25));
        Close.setMinimumSize(new java.awt.Dimension(40, 25));
        Close.setPreferredSize(new java.awt.Dimension(50, 25));
        Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Close(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(Close, gridBagConstraints);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel4.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Desktop.background"));
        getContentPane().add(jPanel4, java.awt.BorderLayout.EAST);

        jPanel5.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Desktop.background"));
        getContentPane().add(jPanel5, java.awt.BorderLayout.WEST);

        pack();
    }//GEN-END:initComponents

    private void Help(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Help
        // Add your handling code here:
       this.ui.doHelp( evt );
       
    }//GEN-LAST:event_Help
    
    private void restoreDefaults(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreDefaults
        // Add your handling code here:
        // get default hosts from ui
        // populate fields
        // write prefs file if exists
        
        String swanHosts = ui.getDefaultSwanHosts();
        String[] swanList = swanHosts.split(delimiter);
        
        String internetHosts = ui.getDefaultInternetHosts();
        String[] internetList = internetHosts.split(delimiter);
        
        SwanHost1Field.setText("");
        SwanHost2Field.setText("");
        SwanHost3Field.setText("");
        
        for ( int i=0; i<swanList.length; i++ ){
            if ( i == maxIndex ) break;
            switch (i) {
                case 0:
                    
                    SwanHost1Field.setText(swanList[i]);
                    SwanHostList.add(i, swanList[i]);
                    break;
                case 1:
                    
                    SwanHost2Field.setText(swanList[i]);
                    SwanHostList.add(i, swanList[i]);
                    break;
                case 2:
                    
                    SwanHost3Field.setText(swanList[i]);
                    SwanHostList.add(i, swanList[i]);
                    break;
            }
        }
        InternetHost1Field.setText("");
        InternetHost2Field.setText("");
        InternetHost3Field.setText("");
        for ( int i=0; i<internetList.length; i++ ){
            if ( i == maxIndex ) break;
            switch (i) {
                case 0:
                    
                    InternetHost1Field.setText(internetList[i]);
                    InternetHostList.add( i, internetList[i] );
                    break;
                case 1:
                    
                    InternetHost2Field.setText(internetList[i]);
                    InternetHostList.add( i, internetList[i] );
                    break;
                case 2:
                    
                    InternetHost3Field.setText(internetList[i]);
                    InternetHostList.add( i, internetList[i] );
                    break;
            }
        }
        
        
        
    }//GEN-LAST:event_restoreDefaults
    
    private void Close(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Close
        // Add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_Close
    
    private void Save(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Save
        // Add your handling code here:
        
        // set values in NetStatUI
        // get values in fields
        String SwanTxt;
        String InternetTxt;
        SwanHostList = new Vector();
        InternetHostList = new Vector();
        for ( int i=0; i<maxIndex; i++ ){
            switch ( i ) {
                case 0: try{
                    InternetTxt= InternetHost1Field.getText();
                } catch ( NullPointerException np ){
                    continue;
                }
                if ( InternetTxt.length() > 0 ) 
                InternetHostList.add( InternetTxt );
                break;
                
                case 1:try{
                    InternetTxt = InternetHost2Field.getText();
                } catch ( NullPointerException np ){
                    continue;
                }
                if ( InternetTxt.length() > 0 ) 
                InternetHostList.add( InternetTxt );
                break;
                
                case 2: try{
                    InternetTxt = InternetHost3Field.getText();
                } catch ( NullPointerException np ){
                    continue;
                }
                if ( InternetTxt.length() > 0 ) 
                InternetHostList.add( InternetTxt );
                break;
                
            }
        }
        for ( int i=0; i<maxIndex; i++ ){
            switch ( i ) {
                case 0: try{
                    SwanTxt= SwanHost1Field.getText();
                } catch ( NullPointerException np ){
                    continue;
                }
                if ( SwanTxt.length() > 0 )
                SwanHostList.add( SwanTxt );
                break;
                
                case 1:try{
                    SwanTxt = SwanHost2Field.getText();
                } catch ( NullPointerException np ){
                    continue;
                }
                if ( SwanTxt.length() > 0 )
                SwanHostList.add( SwanTxt );
                break;
                
                case 2: try{
                    SwanTxt = SwanHost3Field.getText();
                } catch ( NullPointerException np ){
                    continue;
                }
                if ( SwanTxt.length() > 0 )
                SwanHostList.add( SwanTxt );
                break;
                
            }
        }
        
        ui.setSwanHosts(SwanHostList);
        ui.setInternetHosts(InternetHostList);
        
        try{
            
            FileWriter fw = new FileWriter( prefsFileName );
            BufferedWriter bw = new BufferedWriter( fw );
            // write prefs file
            // add delimiter back
            StringBuffer buf = new StringBuffer();
            int indexLen = SwanHostList.size();
            for ( Enumeration en = SwanHostList.elements(); en.hasMoreElements(); ){
                
                buf.append((String)en.nextElement());
                if ( en.hasMoreElements() ) {
                    buf.append(delimiter);
                }
            }
            bw.write(SwanKey + " " + buf.toString() + "\n" );
            
            buf = new StringBuffer();
            
            for ( Enumeration en = InternetHostList.elements(); en.hasMoreElements(); ){
                buf.append((String)en.nextElement());
                if (  en.hasMoreElements()) {
                    buf.append(delimiter);
                }
            }
            bw.write( InternetKey + " " + buf.toString() + "\n" );
            bw.close();
        } catch ( java.io.FileNotFoundException fnf ){
            
        } catch ( java.io.IOException ioe ){
            
        }
         this.Close(evt);
        
    }//GEN-LAST:event_Save
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new EditPrefs().show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton Close;
    protected javax.swing.JButton Help;
    protected javax.swing.JTextField InternetHost1Field;
    protected javax.swing.JLabel InternetHost1Label;
    protected javax.swing.JTextField InternetHost2Field;
    protected javax.swing.JLabel InternetHost2Label;
    protected javax.swing.JTextField InternetHost3Field;
    protected javax.swing.JLabel InternetHost3Label;
    protected javax.swing.JButton Restore;
    protected javax.swing.JButton Save;
    protected javax.swing.JTextField SwanHost1Field;
    protected javax.swing.JLabel SwanHost1Label;
    protected javax.swing.JTextField SwanHost2Field;
    protected javax.swing.JLabel SwanHost2Label;
    protected javax.swing.JTextField SwanHost3Field;
    protected javax.swing.JLabel SwanHost3Label;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JPanel jPanel3;
    protected javax.swing.JPanel jPanel4;
    protected javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables
    
}
