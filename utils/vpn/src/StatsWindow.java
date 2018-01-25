/*
 * StatsWindow.java
 *
 * Created on December 5, 2003, 2:15 PM
 */

/**
 *
 * @author  pdel
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.*;

public class StatsWindow extends javax.swing.JFrame implements ComponentListener {
    
    /** Creates new form StatsWindow */
    public StatsWindow() {
        initComponents();
        this.pack();
        //populate();
        this.update();
    }
    
    
    public StatsWindow(vpn3k GUI) {
        UI=GUI;
        setIconImage(UI.minimizeIcon.getImage());
        initComponents();
        this.pack();
        //status = new vpnStat();
        //UI.Stats = status.getInfo();
        //System.out.println ( "Status: " + UI.Stats );
        //populate();
        setLocation();
        this.update();
      }
    
    public void getFocus(){
        setFocusTraversalKeysEnabled(true);
        setFocusableWindowState(true);
        setFocusable(true);
        requestFocusInWindow();
        CloseButton.requestFocus();
        setVisible(true);
    }
    public void setLocation(){
        Point p = UI.getLocation();
        Dimension uiSize = UI.getSize();
        
        double x = p.getX();
        double y = p.getY();
        
        y = y/2;
        x = (int)x + ( uiSize.width / 2 );
        setLocation((int)x, (int)y );
    }
    public void stopUpdates(){
        
        System.out.println( "stopUpdates()" );
        this.resetStats(null);
        this.connectionTimer.cancel();
        
        this.updating = false;
        this.UI.stopProgressBar();
        
        
    }
    public String getVpnGateway(){
        return this.ServerAddressField.getText();
    }
    public String getClientAddress(){
        return this.ClientAddressField.getText();
    }
    protected void update(){
        if ( this.UI.initialStartup == true ){
            this.UI.updateStatus("Checking Connection Status .." );
            this.UI.startProgressBar();
        }
        
        if ( connectionTimer instanceof Timer ){
            this.resetStats(null);
            connectionTimer.cancel();
        }
        
        
        try{
            connectionTimer = new Timer();
            connectionTimer.scheduleAtFixedRate( new updateThread( status, this, "updating", connectionTimer ), UpdateInterval, UpdateInterval );
            this.updating = true;
        } catch ( Exception ie ){
            this.updating = false;
        }
    }
    public String createConnectedStatus(String gatewayIP){
        // Set UI status
        
        nslookup ns = new nslookup();
        Vector ns_results = ns.lookup(gatewayIP);
        String gatewayName = "";
        if ( ns_results.size() > 0 ){
            gatewayName = (String)ns_results.get(0);
        }
        String status = "";
        if ( gatewayName.length() > 0 ){
            status = new String( "Connected to " + gatewayName + " (" + gatewayIP + ")" );
            UI.setConnectionStatus(true);
            UI.enableDisconnectButton();
        } else {
            if ( ! UI.isConnected() ){
                UI.setConnectionStatus(false);
                UI.enableConnectButton();
                return "No Connection (1)" ;
            }
            status = new String( "Cannot get hostname for VPN gateway " + gatewayIP  + " StatsWindow (1) "  );
        }
        return status;
    }
    public String createConnectedStatus(){
        // Set UI status
        
        String gatewayIP = this.getVpnGateway();
        nslookup ns = new nslookup();
        Vector ns_results = ns.lookup(gatewayIP);
        String gatewayName = "";
        if ( ns_results.size() > 0 ){
            gatewayName = (String)ns_results.get(0);
        }
        String status = "";
        if ( gatewayName.length() > 0 ){
            status = new String( "Connected to " + gatewayName + " (" + gatewayIP + ")" );
            UI.setConnectionStatus(true);
            UI.enableDisconnectButton();
        } else {
            if ( ! UI.isConnected() ){
                UI.setConnectionStatus(false);
                UI.enableConnectButton();
                return "No Connection (2)" ;
            }
            //status = new String( "Connected to " + gatewayIP  );
            status = new String( "Cannot get hostname for VPN gateway " + gatewayIP  + " StatsWindow (2) "  );
        }
        return status;
    }
    protected synchronized void populate() {
        
        
        status = new vpnStat();
        
        try {
            status.join();
        } catch (  java.lang.InterruptedException iex ){
        }
        System.out.println( "Status: " + status.getInfo() );
        
        Hashtable StatCheck = status.getInfo();
        String StatusString = "";
        StatusString = (String)StatCheck.get("Status");
        
        if ( StatusString == null ) {
            return;
        }
        
        if ( StatusString.compareTo("active") != 0  ){
            this.ClientAddressField.setText( "" );
            this.ServerAddressField.setText( "");
            this.ConnectionEntryField.setText( "");
            this.ConnectionTimeField.setText( "" );
            this.BytesReceivedField.setText( "");
            this.BytesSentField.setText( "" );
            this.EncryptionField.setText( "" );
            this.AuthenticationField.setText( "" );
            this.DiscardedPacketField.setText( "" );
            this.EncryptedPacketField.setText( "" );
            this.DecryptedPacketField.setText( "" );
            this.BypassedPacketField.setText( "" );
            this.LocalLanField.setText( "" );
            this.TransparentTunnelingField.setText( "" );
            this.CompressionField.setText( "" );
            return;
        }
        
        Hashtable TunnelInfo =  (Hashtable)status.statusItems.get(StatHeadings.VPN_TUNNEL);
        Hashtable TrafficInfo =  (Hashtable)status.statusItems.get(StatHeadings.VPN_TRAFFIC);
        Hashtable RouteInfo =  (Hashtable)status.statusItems.get(StatHeadings.VPN_ROUTES);
        
        
        this.ClientAddressField.setText( (String)TunnelInfo.get( StatFields.CLIENT_ADDRESS ));
        this.ServerAddressField.setText( (String)TunnelInfo.get( StatFields.SERVER_ADDRESS ));
        this.ConnectionEntryField.setText( (String)TunnelInfo.get(StatFields.CONNECTION_ENTRY ));
        this.ConnectionTimeField.setText( (String)TrafficInfo.get( StatFields.TIME_CONNECTED ));
        this.BytesReceivedField.setText( (String)TrafficInfo.get( StatFields.BYTES_IN ));
        this.BytesSentField.setText( (String)TrafficInfo.get( StatFields.BYTES_OUT ));
        this.EncryptionField.setText( (String)TunnelInfo.get( StatFields.ENCRYPTION ));
        this.AuthenticationField.setText( (String)TunnelInfo.get( StatFields.AUTHENTICATION ));
        this.DiscardedPacketField.setText( (String)TrafficInfo.get( StatFields.PACKETS_DISCARDED ));
        this.EncryptedPacketField.setText( (String)TrafficInfo.get( StatFields.PACKETS_ENCRYPTED  ));
        this.DecryptedPacketField.setText( (String)TrafficInfo.get( StatFields.PACKETS_DECRYPTED ));
        this.BypassedPacketField.setText( (String)TrafficInfo.get( StatFields.PACKETS_BYPASSED  ));
        this.LocalLanField.setText( (String)TunnelInfo.get( StatFields.NAT_PASSTHROUGH ));
        this.TransparentTunnelingField.setText( (String)TunnelInfo.get( StatFields.TRANSPARENT_TUNNELING ));
        this.CompressionField.setText((String)TunnelInfo.get( StatFields.IP_COMPRESSION ));
        
        // update UI status
        UI.updateStatus(  createConnectedStatus() );
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
        
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        TunnelTabPane = new javax.swing.JPanel();
        AddressPane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ClientAddressField = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ServerAddressField = new javax.swing.JLabel();
        ConnectionPane = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ConnectionEntryField = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        ConnectionTimeField = new javax.swing.JLabel();
        BytePane = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        BytesReceivedField = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        BytesSentField = new javax.swing.JLabel();
        CryptoPane = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        EncryptionField = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        AuthenticationField = new javax.swing.JLabel();
        PacketPane = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        EncryptedPacketField = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        DecryptedPacketField = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        DiscardedPacketField = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        BypassedPacketField = new javax.swing.JLabel();
        TransportPane = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        TransparentTunnelingField = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        LocalLanField = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        CompressionField = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        ResetButtonPane = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        CloseButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        setTitle("ITvpntool VPN 3000 Client  |  Statistics");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(500, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 250));
        jTabbedPane1.setFocusable(false);
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(500, 250));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(450, 250));
        TunnelTabPane.setLayout(new java.awt.GridBagLayout());

        TunnelTabPane.setMinimumSize(new java.awt.Dimension(300, 200));
        TunnelTabPane.setPreferredSize(new java.awt.Dimension(300, 200));
        AddressPane.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel1.setText("Address Information");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AddressPane.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel2.setText("Client: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        AddressPane.add(jLabel2, gridBagConstraints);

        ClientAddressField.setFont(new java.awt.Font("Dialog", 0, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        AddressPane.add(ClientAddressField, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel4.setText("Server:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        AddressPane.add(jLabel4, gridBagConstraints);

        ServerAddressField.setFont(new java.awt.Font("Dialog", 0, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        AddressPane.add(ServerAddressField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        TunnelTabPane.add(AddressPane, gridBagConstraints);

        ConnectionPane.setLayout(new java.awt.GridBagLayout());

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel6.setText("Connection Information");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        ConnectionPane.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel7.setText("Entry:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        ConnectionPane.add(jLabel7, gridBagConstraints);

        ConnectionEntryField.setFont(new java.awt.Font("Dialog", 0, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        ConnectionPane.add(ConnectionEntryField, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel9.setText("Time:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        ConnectionPane.add(jLabel9, gridBagConstraints);

        ConnectionTimeField.setFont(new java.awt.Font("Dialog", 0, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        ConnectionPane.add(ConnectionTimeField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 10, 50);
        TunnelTabPane.add(ConnectionPane, gridBagConstraints);

        BytePane.setLayout(new java.awt.GridBagLayout());

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel11.setText("Bytes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        BytePane.add(jLabel11, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel12.setText("Received:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        BytePane.add(jLabel12, gridBagConstraints);

        BytesReceivedField.setFont(new java.awt.Font("Dialog", 0, 10));
        BytesReceivedField.setText("Bytes Received");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        BytePane.add(BytesReceivedField, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel14.setText("Sent:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        BytePane.add(jLabel14, gridBagConstraints);

        BytesSentField.setFont(new java.awt.Font("Dialog", 0, 10));
        BytesSentField.setText("Bytes Sent");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        BytePane.add(BytesSentField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        TunnelTabPane.add(BytePane, gridBagConstraints);

        CryptoPane.setLayout(new java.awt.GridBagLayout());

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel16.setText("Crypto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        CryptoPane.add(jLabel16, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel17.setText("Encryption:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        CryptoPane.add(jLabel17, gridBagConstraints);

        EncryptionField.setFont(new java.awt.Font("Dialog", 0, 10));
        EncryptionField.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        CryptoPane.add(EncryptionField, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel19.setText("Authentication:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        CryptoPane.add(jLabel19, gridBagConstraints);

        AuthenticationField.setFont(new java.awt.Font("Dialog", 0, 10));
        AuthenticationField.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        CryptoPane.add(AuthenticationField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 10, 50);
        TunnelTabPane.add(CryptoPane, gridBagConstraints);

        PacketPane.setLayout(new java.awt.GridBagLayout());

        jLabel21.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel21.setText("Packets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        PacketPane.add(jLabel21, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel22.setText("Encrypted");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        PacketPane.add(jLabel22, gridBagConstraints);

        EncryptedPacketField.setFont(new java.awt.Font("Dialog", 0, 10));
        EncryptedPacketField.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        PacketPane.add(EncryptedPacketField, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel24.setText("Decrypted");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        PacketPane.add(jLabel24, gridBagConstraints);

        DecryptedPacketField.setFont(new java.awt.Font("Dialog", 0, 10));
        DecryptedPacketField.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        PacketPane.add(DecryptedPacketField, gridBagConstraints);

        jLabel31.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel31.setText("Discarded");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        PacketPane.add(jLabel31, gridBagConstraints);

        DiscardedPacketField.setFont(new java.awt.Font("Dialog", 0, 10));
        DiscardedPacketField.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        PacketPane.add(DiscardedPacketField, gridBagConstraints);

        jLabel33.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel33.setText("Bypassed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        PacketPane.add(jLabel33, gridBagConstraints);

        BypassedPacketField.setFont(new java.awt.Font("Dialog", 0, 10));
        BypassedPacketField.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        PacketPane.add(BypassedPacketField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        TunnelTabPane.add(PacketPane, gridBagConstraints);

        TransportPane.setLayout(new java.awt.GridBagLayout());

        jLabel26.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel26.setText("Transport");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        TransportPane.add(jLabel26, gridBagConstraints);

        jLabel27.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel27.setText("Transparent Tunneling:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        TransportPane.add(jLabel27, gridBagConstraints);

        TransparentTunnelingField.setFont(new java.awt.Font("Dialog", 0, 10));
        TransparentTunnelingField.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        TransportPane.add(TransparentTunnelingField, gridBagConstraints);

        jLabel29.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel29.setText("Local Lan:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        TransportPane.add(jLabel29, gridBagConstraints);

        LocalLanField.setFont(new java.awt.Font("Dialog", 0, 10));
        LocalLanField.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        TransportPane.add(LocalLanField, gridBagConstraints);

        jLabel35.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel35.setText("Compression:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        TransportPane.add(jLabel35, gridBagConstraints);

        CompressionField.setFont(new java.awt.Font("Dialog", 0, 10));
        CompressionField.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        TransportPane.add(CompressionField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 10, 50);
        TunnelTabPane.add(TransportPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        TunnelTabPane.add(jPanel6, gridBagConstraints);

        ResetButtonPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton1.setText("Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetStats(evt);
            }
        });

        ResetButtonPane.add(jButton1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 25);
        TunnelTabPane.add(ResetButtonPane, gridBagConstraints);

        jTabbedPane1.addTab("Tunnel Details", TunnelTabPane);

        jTabbedPane1.addTab("Route Details", jPanel4);

        jPanel1.add(jTabbedPane1, new java.awt.GridBagConstraints());

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 30, 5));

        CloseButton.setMnemonic('c');
        CloseButton.setText("Close");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeWindow(evt);
            }
        });

        jPanel2.add(CloseButton);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents
    
    protected void resetStats(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetStats
        // Add your handling code here:
        System.out.println( "resetStats" );
        new vpnStat( "reset" );
        this.ClientAddressField.setText( "" );
        this.ServerAddressField.setText( "");
        this.ConnectionEntryField.setText( "");
        this.ConnectionTimeField.setText( "" );
        this.BytesReceivedField.setText( "");
        this.BytesSentField.setText( "" );
        this.EncryptionField.setText( "" );
        this.AuthenticationField.setText( "" );
        this.DiscardedPacketField.setText( "" );
        this.EncryptedPacketField.setText( "" );
        this.DecryptedPacketField.setText( "" );
        this.BypassedPacketField.setText( "" );
        this.LocalLanField.setText( "" );
        this.TransparentTunnelingField.setText( "" );
        this.CompressionField.setText( "" );
        this.validate();
        //this.populate();
        //this.repaint();
        
        
    }//GEN-LAST:event_resetStats
    
    private void closeWindow(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeWindow
        // Add your handling code here:
        if ( this.isVisible() ){
            this.setVisible(false);
        }
        
        //this.populate();
    }//GEN-LAST:event_closeWindow
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        if ( this.isVisible() == true ) {
            this.setVisible(false);
        }
        
        //this.UI.repaint();
        //this.populate();
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new StatsWindow().show();
    }
    
    public void componentHidden(ComponentEvent e) {
    }    
    
    public void componentMoved(ComponentEvent e) {
    }
    
    public void componentResized(ComponentEvent e) {
    }
    
    public void componentShown(ComponentEvent e) {
        getFocus();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JPanel AddressPane;
    protected javax.swing.JLabel AuthenticationField;
    protected javax.swing.JLabel BypassedPacketField;
    protected javax.swing.JPanel BytePane;
    protected javax.swing.JLabel BytesReceivedField;
    protected javax.swing.JLabel BytesSentField;
    protected javax.swing.JLabel ClientAddressField;
    protected javax.swing.JButton CloseButton;
    protected javax.swing.JLabel CompressionField;
    protected javax.swing.JLabel ConnectionEntryField;
    protected javax.swing.JPanel ConnectionPane;
    protected javax.swing.JLabel ConnectionTimeField;
    protected javax.swing.JPanel CryptoPane;
    protected javax.swing.JLabel DecryptedPacketField;
    protected javax.swing.JLabel DiscardedPacketField;
    protected javax.swing.JLabel EncryptedPacketField;
    protected javax.swing.JLabel EncryptionField;
    protected javax.swing.JLabel LocalLanField;
    protected javax.swing.JPanel PacketPane;
    protected javax.swing.JPanel ResetButtonPane;
    protected javax.swing.JLabel ServerAddressField;
    protected javax.swing.JLabel TransparentTunnelingField;
    protected javax.swing.JPanel TransportPane;
    protected javax.swing.JPanel TunnelTabPane;
    protected javax.swing.JButton jButton1;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel11;
    protected javax.swing.JLabel jLabel12;
    protected javax.swing.JLabel jLabel14;
    protected javax.swing.JLabel jLabel16;
    protected javax.swing.JLabel jLabel17;
    protected javax.swing.JLabel jLabel19;
    protected javax.swing.JLabel jLabel2;
    protected javax.swing.JLabel jLabel21;
    protected javax.swing.JLabel jLabel22;
    protected javax.swing.JLabel jLabel24;
    protected javax.swing.JLabel jLabel26;
    protected javax.swing.JLabel jLabel27;
    protected javax.swing.JLabel jLabel29;
    protected javax.swing.JLabel jLabel31;
    protected javax.swing.JLabel jLabel33;
    protected javax.swing.JLabel jLabel35;
    protected javax.swing.JLabel jLabel4;
    protected javax.swing.JLabel jLabel6;
    protected javax.swing.JLabel jLabel7;
    protected javax.swing.JLabel jLabel9;
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JPanel jPanel4;
    protected javax.swing.JPanel jPanel6;
    protected javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
    protected vpn3k UI;
    protected vpnStat status;
    private static final long UpdateInterval = 30000;
    protected Timer connectionTimer;
    private boolean updating = false;
    //protected ProgressThread ProgressIndicator;
    
}

class updateThread extends TimerTask {
    
    protected vpnStat status;
    protected StatsWindow wind;
    protected String[] notificationInfo;
    // constructor
    
    String description;
    Timer timer;
    
    public updateThread( vpnStat stat, StatsWindow win, String descr, Timer t  ){
        System.out.println( "Update thread" );
        description = descr;
        timer = t;
        status=stat;
        wind=win;
        run();
    }
    
    public updateThread( vpnStat stat, StatsWindow win ){
        status=stat;
        wind=win;
        
    }
    
    
    
    // run
    public void run(){
        
        System.out.println( "Status Window running" );
        
        try {
            
            status = null;
            status = new vpnStat();
            status.join();
            if ( wind.UI instanceof vpn3k ){
                vpnNotify notification = new vpnNotify();
                notification.join();
                //notification.printNotification();
                notificationInfo = notification.getNotificationInfo();
            }
            
        } catch (  java.lang.InterruptedException iex ){
        }
        
        if ( wind.UI instanceof vpn3k ){
            wind.UI.NotificationInfo = notificationInfo;
        }
        
        System.out.println( "Status: " + status.getInfo() );
        
        Hashtable StatCheck = status.getInfo();
        String StatusString = "";
        StatusString = (String)StatCheck.get("Status");
        
        if ( StatusString == null ) {
            return;
        }
        
        if ( StatusString.compareTo("active") != 0  ){
            wind.ClientAddressField.setText( "" );
            wind.ServerAddressField.setText( "");
            wind.ConnectionEntryField.setText( "");
            wind.ConnectionTimeField.setText( "" );
            wind.BytesReceivedField.setText( "");
            wind.BytesSentField.setText( "" );
            wind.EncryptionField.setText( "" );
            wind.AuthenticationField.setText( "" );
            wind.DiscardedPacketField.setText( "" );
            wind.EncryptedPacketField.setText( "" );
            wind.DecryptedPacketField.setText( "" );
            wind.BypassedPacketField.setText( "" );
            wind.LocalLanField.setText( "" );
            wind.TransparentTunnelingField.setText( "" );
            wind.CompressionField.setText( "" );
            wind.repaint();
            
            if ( wind.UI instanceof vpn3k ){
                if ( wind.UI.initialStartup == true ){
                    wind.UI.Connected = false;
                    wind.UI.initialStartup = false;
                    String status = new String( "No Connection" );
                    if ( ! wind.UI.Connecting ){
                        wind.UI.updateStatus( status );
                        wind.UI.stopProgressBar();
                    }
                } else {
                    if ( wind.UI.Connected == true ){
                        wind.UI.Connected = false;
                        String status = new String( "No Connection" );
                        if ( ! wind.UI.Connecting ){
                            wind.UI.updateStatus( status );
                            wind.UI.stopProgressBar();
                        }
                    }
                }
                
                wind.UI.setConnectButton();
                
                
            }
            return;
        }
        
        
        
        Hashtable TunnelInfo =  (Hashtable)status.statusItems.get(StatHeadings.VPN_TUNNEL);
        Hashtable TrafficInfo =  (Hashtable)status.statusItems.get(StatHeadings.VPN_TRAFFIC);
        Hashtable RouteInfo =  (Hashtable)status.statusItems.get(StatHeadings.VPN_ROUTES);
        
        
        wind.ClientAddressField.setText( (String)TunnelInfo.get( StatFields.CLIENT_ADDRESS ));
        wind.ServerAddressField.setText( (String)TunnelInfo.get( StatFields.SERVER_ADDRESS ));
        wind.ConnectionEntryField.setText( (String)TunnelInfo.get(StatFields.CONNECTION_ENTRY ));
        wind.ConnectionTimeField.setText( (String)TrafficInfo.get( StatFields.TIME_CONNECTED ));
        wind.BytesReceivedField.setText( (String)TrafficInfo.get( StatFields.BYTES_IN ));
        wind.BytesSentField.setText( (String)TrafficInfo.get( StatFields.BYTES_OUT ));
        wind.EncryptionField.setText( (String)TunnelInfo.get( StatFields.ENCRYPTION ));
        wind.AuthenticationField.setText( (String)TunnelInfo.get( StatFields.AUTHENTICATION ));
        wind.DiscardedPacketField.setText( (String)TrafficInfo.get( StatFields.PACKETS_DISCARDED ));
        wind.EncryptedPacketField.setText( (String)TrafficInfo.get( StatFields.PACKETS_ENCRYPTED  ));
        wind.DecryptedPacketField.setText( (String)TrafficInfo.get( StatFields.PACKETS_DECRYPTED ));
        wind.BypassedPacketField.setText( (String)TrafficInfo.get( StatFields.PACKETS_BYPASSED  ));
        wind.LocalLanField.setText( (String)TunnelInfo.get( StatFields.NAT_PASSTHROUGH ));
        wind.TransparentTunnelingField.setText( (String)TunnelInfo.get( StatFields.TRANSPARENT_TUNNELING ));
        wind.CompressionField.setText((String)TunnelInfo.get( StatFields.IP_COMPRESSION ));
        if ( wind.UI instanceof vpn3k ){
            //String status = new String( "Connected to " + wind.ConnectionEntryField.getText() );
            System.out.println( "Update Thread Connection Status: (1) " );
            String status = wind.createConnectedStatus();
            wind.UI.updateStatus( status );
            
            if ( ! wind.UI.Connected  && wind.UI.initialStartup ){
                wind.UI.Connected = true;
            } else  if ( wind.UI.initialStartup == true && ! wind.UI.Connected ){
                    wind.UI.Connected = true;
                    wind.UI.initialStartup = false;
                    //status = new String( "Connected to " + wind.ConnectionEntryField.getText() );
                
            }
            wind.UI.setConnectButton();
            wind.UI.stopProgressBar();
        }
        wind.repaint();
        //timer.cancel();
        
    }
}
