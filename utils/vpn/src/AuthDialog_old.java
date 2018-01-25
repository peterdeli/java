/*
 * AuthDialog.java
 *
 * Created on October 28, 2003, 10:21 PM
 */

/**
 *
 * @author  pdel
 */

import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class AuthDialog_old extends javax.swing.JDialog {
    
    /** Creates new form AuthDialog */
    public AuthDialog_old() {
        initComponents();
        addTextFieldListeners();
        setComponentFocus( this.TokenResponse );
        
    }
    
    public AuthDialog_old(JFrame parent, boolean modal) {
        initComponents();
        addTextFieldListeners();
        setComponentFocus( this.TokenResponse );
    }
    
    public AuthDialog_old(vpn3k GUI) {
        
        super( new javax.swing.JFrame(), true );
        initComponents();
        addTextFieldListeners();
        UI=GUI;
        UI.setLookAndFeel();
        UserID.setText(UI.getSelectedAcctAttribute("Username"));
        setComponentFocus( this.TokenResponse );
        //UserID.selectAll();
        this.setLocationRelativeTo(UI);
        this.show();
    }
    
    public AuthDialog_old(vpn3k GUI, String acct) {
        
        super( new javax.swing.JFrame(), true );
        initComponents();
        addTextFieldListeners();
        UI=GUI;
        UI.setLookAndFeel();
        UserID.setText(UI.getSelectedAcctAttribute("Username"));
        UserID.selectAll();
        acctName = acct;
        setComponentFocus( this.TokenResponse );
        this.setLocationRelativeTo(UI);
        this.show();
    }
    
    public void run() {
        this.show();
    }
    
    private void setComponentFocus( Component comp ){
        final Component c;
        c= comp;
        this.addWindowListener( new WindowAdapter() {
            public void windowOpened( WindowEvent e ){
                c.requestFocus();
            }
        } );
        
    }
    private void updateConnectButton(){
        
        if ( UserID.getText().length() > 0 && TokenResponse.getPassword().length > 0 ){
            ConnectButton.setEnabled(true);
        } else {
            
            ConnectButton.setEnabled(false);
        }
    }
    private void addTextFieldListeners(){
        Document userIDdoc = this.UserID.getDocument();
        
        userIDdoc.addDocumentListener(
        new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                // when text/attributes change...
                //System.out.println( "attribute change for userID" );
                updateConnectButton();
            }
            public void insertUpdate(DocumentEvent e) {
                // when text inserted...
                //System.out.println( "text inserted for userID" );
                updateConnectButton();
                
            }
            public void removeUpdate(DocumentEvent e) {
                // when text removed...
                //System.out.println( "text removed for userID" );
                updateConnectButton();
            }
        });
        
        Document TokenResponseDoc = this.TokenResponse.getDocument();
        
        TokenResponseDoc.addDocumentListener(
        new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                // when text/attributes change...
                //System.out.println( "attribute change for TokenResponse Field" );
                updateConnectButton();
            }
            public void insertUpdate(DocumentEvent e) {
                // when text inserted...
                //System.out.println( "text inserted for TokenResponse Field" );
                updateConnectButton();
                
            }
            public void removeUpdate(DocumentEvent e) {
                // when text removed...
                //System.out.println( "text removed for TokenResponse Field" );
                updateConnectButton();
            }
        });
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
        jLabel2 = new javax.swing.JLabel();
        UserID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TokenResponse = new javax.swing.JPasswordField();
        jPanel4 = new javax.swing.JPanel();
        ConnectButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridLayout(3, 0, 5, 0));

        setTitle("VPN Authentication");
        setBackground(new java.awt.Color(190, 190, 226));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ProcessKeyTyped(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                CloseDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 15));

        jPanel1.setBackground(new java.awt.Color(190, 190, 226));
        jPanel1.setMinimumSize(new java.awt.Dimension(400, 30));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 30));
        jLabel1.setBackground(new java.awt.Color(190, 190, 226));
        jLabel1.setText("Enter your VPN ID and TokenCard Challenge\n");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(190, 190, 226));
        jPanel2.setMinimumSize(new java.awt.Dimension(400, 40));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 40));
        jLabel2.setBackground(new java.awt.Color(190, 190, 226));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("VPN User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        jPanel2.add(jLabel2, gridBagConstraints);

        UserID.setColumns(20);
        UserID.setMargin(new java.awt.Insets(0, 5, 0, 5));
        UserID.setMinimumSize(new java.awt.Dimension(234, 19));
        UserID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateAction(evt);
            }
        });
        UserID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                validateEntries(evt);
                ProcessKeyTyped(evt);
            }
        });
        UserID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                validateField(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel2.add(UserID, gridBagConstraints);

        jLabel3.setBackground(new java.awt.Color(190, 190, 226));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("TokenCard Challenge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        jPanel2.add(jLabel3, gridBagConstraints);

        TokenResponse.setColumns(20);
        TokenResponse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TokenResponseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel2.add(TokenResponse, gridBagConstraints);

        getContentPane().add(jPanel2);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 10));

        jPanel4.setBackground(new java.awt.Color(190, 190, 226));
        jPanel4.setMinimumSize(new java.awt.Dimension(155, 20));
        jPanel4.setPreferredSize(new java.awt.Dimension(155, 20));
        ConnectButton.setText("OK");
        ConnectButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        ConnectButton.setMargin(new java.awt.Insets(20, 10, 20, 10));
        ConnectButton.setMinimumSize(new java.awt.Dimension(65, 20));
        ConnectButton.setPreferredSize(new java.awt.Dimension(65, 20));
        ConnectButton.setEnabled(false);
        ConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getResponse(evt);
            }
        });

        jPanel4.add(ConnectButton);

        CancelButton.setMnemonic('C');
        CancelButton.setText("Cancel");
        CancelButton.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        CancelButton.setMargin(new java.awt.Insets(5, 15, 5, 15));
        CancelButton.setMinimumSize(new java.awt.Dimension(65, 20));
        CancelButton.setPreferredSize(new java.awt.Dimension(65, 20));
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel(evt);
            }
        });

        jPanel4.add(CancelButton);

        getContentPane().add(jPanel4);

        pack();
    }//GEN-END:initComponents

    private void TokenResponseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TokenResponseActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_TokenResponseActionPerformed
    
    private void CloseDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_CloseDialog
        // Add your handling code here:
        
        UI.setConnectionStatus(false);
        // enable UI buttons
        UI.setConnectButton();
        this.dispose();
    }//GEN-LAST:event_CloseDialog
    
    private void ProcessKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProcessKeyTyped
        // Add your handling code here:
        // check if ok button enabled
        if ( evt.getKeyChar() == evt.VK_ENTER &&  this.ConnectButton.isEnabled( ) ) {
            this.getResponse(evt);
        }
    }//GEN-LAST:event_ProcessKeyTyped
    
    private void Cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel
        // Add your handling code here:
        // close window
        UI.updateStatus("Connection cancelled");
        UI.updateLog("\nConnection cancelled\n");
        //UI.vpnTerminate();
        
        UI.Connected = false;
        UI.Connecting = false;
        UI.setConnectionStatus(false);
        //UI.setUserID(null);
        //UI.setTokenPass(null);
        UI.enableConnectButton();
         this.dispose();
        
    }//GEN-LAST:event_Cancel
    
    private void validateAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validateAction
        // Add your handling code here:
        /*
        if ( this.UserID.getText().length() >0  ||
        this.TokenResponse.getText().length() > 0 ){
            this.ConnectButton.setEnabled(true);
        } else {
            this.ConnectButton.setEnabled(false);
        }
         **/
    }//GEN-LAST:event_validateAction
    
    private void validateField(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_validateField
        // Add your handling code here:
        /*
        if ( this.UserID.getText().length() > 0 &&
        this.TokenResponse.getText().length() > 0 ){
            this.ConnectButton.setEnabled(true);
        } else {
            this.ConnectButton.setEnabled(false);
        }
         **/
    }//GEN-LAST:event_validateField
    
    private void validateEntries(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_validateEntries
        // Add your handling code here:
        // check if UserID has text and TokenResponse Does
        
       /*
        if ( this.UserID.getText().length() > 0 &&
        this.TokenResponse.getText().length() > 0 ){
            this.ConnectButton.setEnabled(true);
        } else {
            this.ConnectButton.setEnabled(false);
        }
        **/
        
    }//GEN-LAST:event_validateEntries
    private void getResponse(java.awt.event.KeyEvent evt) {
        // Add your handling code here:
        UI.setUserID( this.UserID.getText() );
        char[] tokenChars = this.TokenResponse.getPassword();
        String tokenString = new String ( tokenChars );
        UI.setTokenPass( tokenString );
        this.dispose();
        
    }
    private void getResponse(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getResponse
        // Add your handling code here:
        UI.setUserID( this.UserID.getText() );
        char[] tokenChars = this.TokenResponse.getPassword();
        String tokenString = new String ( tokenChars );
        UI.setTokenPass( tokenString );
        this.dispose();
        
    }//GEN-LAST:event_getResponse
    
    synchronized void myNotify(){
        this.notifyAll();
    }
    /** Exit the Application */
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new AuthDialog().show();
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton CancelButton;
    protected javax.swing.JButton ConnectButton;
    protected javax.swing.JPasswordField TokenResponse;
    protected javax.swing.JTextField UserID;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel2;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
    vpn3k UI;
    Thread th;
    String acctName;
}