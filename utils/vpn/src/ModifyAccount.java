/*
 * ModifyAccount.java
 *
 * Created on November 9, 2003, 4:49 PM
 */

/**
 *
 * @author  pdel
 */
import java.util.*;
import java.io.*;
import javax.swing.*;


public class ModifyAccount extends NewAccount {
    
    public ModifyAccount(){
        super( new javax.swing.JFrame(), true );
        this.setTitle( title );
    }
    
    
    /** Creates a new instance of ModifyAccount */
    public ModifyAccount(vpn3k GUI, String acct, int rowNumber) {
        //super( new javax.swing.JFrame(), true );
        super( GUI, false );
        //requestFocusInWindow();
        //setVisible(false);
        this.setTitle( title );
        UI = GUI;
        acctName = acct;
        row = rowNumber;
        populateFields();
        this.setLocationRelativeTo(UI);
        getFocus();
        
    }
    
    protected int insertAccount(){
        System.out.println( "Modify" );
        // delete old account, replace with new account
        
        // 1. delete account with old name
        String profileDir = this.UI.getDefaultProfileDir();
        // account should be in here
        Hashtable ht = this.UI.getAccountInfo(acctName);
        String oldAcctFileName = (String)ht.get( "Filename" );
        
        if ( oldAcctFileName != null ){
            try {
                File oldAcct = new File( oldAcctFileName );
                //if ( oldAcct.isFile() ){
                //    oldAcct.delete();
                //}
            } catch ( Exception ex ){
                
                JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error removing profile " );
            }
        }
        // get row of account to be modified
        //int row = this.UI.getSelectedAccountRow();
        
        // 2. create new account from changed values
        ht = super.createAccountInfo();
        String newName = AcctNameField.getText();
        if ( newName.length() < 1 || newName == null ){
            return -1;
        } else if ( (this.UI.accountExists(newName)) && ( acctName.compareTo(newName) != 0) ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Connection Entry " + newName + " already taken" );
            return -1;
        } else {
            this.UI.modifyAccount(acctName, newName, ht, row );
            //this.UI.setDefaultAccountName(newName);
            
        }
        return 0;
    }
    
    
    protected void createAccount(java.awt.event.ActionEvent evt) {
        // 2. create new account from changed values
        
        if ( insertAccount() == 0 ){
            this.dispose();
        }
    }
    
    private void populateFields(){
        
        Hashtable ht = this.UI.getAccountInfo(acctName);
        Vector LockedFields = new Vector();
        
        this.AcctNameField.setText(acctName);
        
        if ( ht.containsKey(ProfileFields.Locked) ){
            Object obj = ht.get(ProfileFields.Locked);
            if ( obj instanceof Vector ){
                LockedFields = (Vector)obj;
            }
        }
        
        String descr = (String)ht.get(ProfileFields.Description );
        if ( descr != null  &&  (descr.length() > 0 )){
            this.DescrField.setText((String)ht.get( ProfileFields.Description  ));
            if ( LockedFields.contains(ProfileFields.Description)){
                this.DescrField.setEnabled(true);
                this.DescrField.setEditable(false);
            }else{
                this.DescrField.setEnabled(true);
            }
        }
        
        String host = (String)ht.get(ProfileFields.Host );
        if ( host != null && host.length() > 0 ){
            this.HostField.setText(host);
            if ( LockedFields.contains(ProfileFields.Host)){
                this.HostField.setEnabled(true);
                this.HostField.setEditable(false);
            }
        }
        
        
        String key = (String)ht.get( ProfileFields.TunnelingMode );
        if ( key == null || key.length() < 1 ){
            this.IPSecUDPButton.setSelected(false);
        } else {
            int udpEnabled = new Integer( key ).intValue();
            if ( udpEnabled == 0 ){
                this.IPSecUDPButton.setSelected(true);
                this.IPSecTCPButton.setSelected(false);
            } else {
                this.IPSecUDPButton.setSelected(false);
                this.IPSecTCPButton.setSelected(true);
            }
            
        }
        key = (String)ht.get(  ProfileFields.PeerTimeout );
        if ( key != null && key.length() > 0  ){
            this.PeerTimeoutField.setText(key);
        }
        
        key = (String)ht.get( ProfileFields.EnableNat );
        if ( key != null && key.length() > 0 ){
            int natEnabled = new Integer( key ).intValue();
            if ( natEnabled == 1 ){
                this.TransparentTunneling.setSelected(true);
            } else {
                this.TransparentTunneling.setSelected(false);
            }
        }
        
        key = (String)ht.get( ProfileFields.EnableLocalLAN );
        if ( key != null && key.length() > 0){
            int localLanEnabled = new Integer( key ).intValue();
            
            if ( localLanEnabled == 1 ){
                this.LocalLanButton.setSelected(true);
            } else {
                this.LocalLanButton.setSelected(false);
            }
            
        }
        
        key = (String)ht.get( ProfileFields.TCPTunnelingPort );
        if ( key != null && key.length() > 0 ){
            this.TcpPortField.setText(key);
        }
        
        if ( this.IPSecTCPButton.isSelected() ){
            super.setTcpPort(true);
        }
        
        key = (String)ht.get( ProfileFields.GroupName );
        if ( key != null && key.length() > 0 ){
            this.GroupField.setText(key);
            if ( LockedFields.contains(ProfileFields.GroupName) ){
                this.GroupField.setEditable(false);
            }else{
                this.GroupField.setEditable(true);
            }
        }
        
        key = (String)ht.get( ProfileFields.GroupPwd );
        if ( key != null && key.length() > 0 ){
            this.GroupPasswdField.setText(key);
            this.GroupPassConfirmField.setText(key);
            if ( LockedFields.contains(ProfileFields.GroupPwd) ){
                this.GroupPasswdField.setEditable(false);
                this.GroupPassConfirmField.setEditable(false);
            }else{
                this.GroupPasswdField.setEditable(true);
                this.GroupPassConfirmField.setEditable(true);
            }
        } else {
            // check encrypted passwd.
            // if exists, means was previously set by groupPwd
            //
        }
        
        key = (String)ht.get( ProfileFields.EnableBackup );
        if ( key != null && key.length() > 0 ){
            if ( LockedFields.contains( ProfileFields.EnableBackup ) ){
                this.EnableBackupServers.setEnabled(false);
            } else {
                this.EnableBackupServers.setEnabled(true);
            }
            
            int enableBackupState = new Integer( key ).intValue();
            switch ( enableBackupState ){
                case 0: this.EnableBackupServers.setSelected(false); break;
                case 1: this.EnableBackupServers.setSelected(true); break;
                default: break;
            }
        }
        
        key = (String)ht.get( ProfileFields.BackupServer );
        if ( key != null && key.length() > 0 ){
            DefaultListModel dm = (DefaultListModel)this.BackupServerList.getModel();
            dm.clear();
            String[] BackupServers = key.split(",");
            for ( int i=0; i<BackupServers.length; i++ ){
                dm.addElement(BackupServers[i]);
            }
        }
        if ( this.IPSecTCPButton.isSelected() ){
            super.setTcpPort(true);
        }
        //(TunnelingMode is 0 for UDP and 1 for TCP (IIRC)
        
        
        /*
         *private javax.swing.JTextField AcctNameField;
         *
         *private javax.swing.JTextField DescrField;
         *private javax.swing.JTextField HostField;
         *private javax.swing.JTextField GroupField;
         *
         *private javax.swing.JPasswordField GroupPasswdField;
         *private javax.swing.JTextField TcpPortField;
         *private javax.swing.JTextField PeerTimeoutField;
         *private javax.swing.JPasswordField GroupPassConfirmField;
         *
         *private javax.swing.JRadioButton CertAuthButton;
         *private javax.swing.JRadioButton GroupAuthButton;
         *
         *private javax.swing.JCheckBox CertChainCheckBox;
         *private javax.swing.JCheckBox TransparentTunneling;
         *private javax.swing.JCheckBox LocalLanButton;
         *
         *private javax.swing.JComboBox CertNameCombo;
         *
         *private javax.swing.JRadioButton IPSecTCPButton;
         *private javax.swing.JRadioButton IPSecUDPButton;
         
         
         **/
        
        
    }
    
    
    private vpn3k UI;
    private String acctName;
    private String title = "VPN Client | Modify VPN Client Connection Entry";
    private int row;
}
