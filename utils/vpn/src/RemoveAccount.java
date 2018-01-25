/*
 * NewAccount.java
 *
 * Created on November 4, 2003, 3:08 PM
 */

/**
 *
 * @author  pdel
 */

import javax.swing.*;
import java.util.*;


public class RemoveAccount extends javax.swing.JDialog {
    
    /** Creates new form NewAccount */
    public RemoveAccount(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        createButtonGroups();
    }
     public RemoveAccount(vpn3k vpnUI, boolean showDialog) {
        super( new javax.swing.JFrame(), true );
        UI = vpnUI;
        initComponents();
        createButtonGroups();
        if ( showDialog == true ) { show(); }
        //show();
        
    }
     public RemoveAccount(vpn3k vpnUI) {
        super( new javax.swing.JFrame(), true );
        UI = vpnUI;
        initComponents();
        createButtonGroups();
        show();
        
    }
    
     public RemoveAccount(vpn3k vpnUI, String acct) {
        super( new javax.swing.JFrame(), true );
        UI = vpnUI;
        acctName = acct;
        initComponents();
        createButtonGroups();
        show();
        
    }
    
    private void createButtonGroups(){
        AuthButtonGroup = new ButtonGroup();
        IPSecButtonGroup = new ButtonGroup();
        
        IPSecButtonGroup.add(this.IPSecUDPButton);
        IPSecButtonGroup.add(this.IPSecTCPButton);
        
        AuthButtonGroup.add( this.GroupAuthButton );
        AuthButtonGroup.add( this.CertAuthButton );
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        AuthButtonGroup = new javax.swing.ButtonGroup();
        IPSecButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        EntryPanel = new javax.swing.JPanel();
        AcctLabel = new javax.swing.JLabel();
        AcctNameField = new javax.swing.JTextField();
        DescriptionPanel = new javax.swing.JPanel();
        DescrLabel = new javax.swing.JLabel();
        DescrField = new javax.swing.JTextField();
        HostPanel = new javax.swing.JPanel();
        HostLabel = new javax.swing.JLabel();
        HostField = new javax.swing.JTextField();
        IconPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        AuthTab = new javax.swing.JPanel();
        GroupAuthPanel = new javax.swing.JPanel();
        GroupAuthButtonPanel = new javax.swing.JPanel();
        GroupAuthButton = new javax.swing.JRadioButton();
        GroupNamePanel = new javax.swing.JPanel();
        GroupLabel = new javax.swing.JLabel();
        GroupField = new javax.swing.JTextField();
        GroupPasswdPanel = new javax.swing.JPanel();
        GroupPasswdLabel = new javax.swing.JLabel();
        GroupPasswdField = new javax.swing.JPasswordField();
        GroupPassConfirmPanel = new javax.swing.JPanel();
        GroupPassConfirmLabel = new javax.swing.JLabel();
        GroupPassConfirmField = new javax.swing.JPasswordField();
        GroupCertPanel = new javax.swing.JPanel();
        GroupAuthButtonPanel1 = new javax.swing.JPanel();
        CertAuthButton = new javax.swing.JRadioButton();
        GroupCertNamePanel = new javax.swing.JPanel();
        CertNameLabel = new javax.swing.JLabel();
        CertNameCombo = new javax.swing.JComboBox();
        CertChainPanel = new javax.swing.JPanel();
        CertChainCheckBox = new javax.swing.JCheckBox();
        TransportTab = new javax.swing.JPanel();
        ProtocolPanel = new javax.swing.JPanel();
        TunnelingPanel = new javax.swing.JPanel();
        TransparentTunneling = new javax.swing.JCheckBox();
        IPSecUDPPanel = new javax.swing.JPanel();
        IPSecUDPButton = new javax.swing.JRadioButton();
        IPSecTCPPanel = new javax.swing.JPanel();
        IPSecTCPButton = new javax.swing.JRadioButton();
        TcpPortLabel = new javax.swing.JLabel();
        TcpPortField = new javax.swing.JTextField();
        NATPanel = new javax.swing.JPanel();
        LanAccessPanel = new javax.swing.JPanel();
        LocalLanButton = new javax.swing.JCheckBox();
        PeerTimoutPanel = new javax.swing.JPanel();
        PeerTimeoutLabel = new javax.swing.JLabel();
        PeerTimeoutField = new javax.swing.JTextField();
        BackupServerTab = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        SaveButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();

        setTitle("VPN Client | Create New VPN Connection Entry");
        setBackground(new java.awt.Color(190, 190, 226));
        setForeground(java.awt.Color.blue);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(190, 190, 226));
        EntryPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        EntryPanel.setBackground(new java.awt.Color(190, 190, 226));
        EntryPanel.setMaximumSize(new java.awt.Dimension(325, 30));
        EntryPanel.setMinimumSize(new java.awt.Dimension(325, 30));
        EntryPanel.setPreferredSize(new java.awt.Dimension(325, 30));
        AcctLabel.setDisplayedMnemonic('C');
        AcctLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        AcctLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        AcctLabel.setText(" Connection Entry:  ");
        AcctLabel.setPreferredSize(new java.awt.Dimension(115, 15));
        EntryPanel.add(AcctLabel);

        AcctNameField.setMaximumSize(new java.awt.Dimension(200, 20));
        AcctNameField.setMinimumSize(new java.awt.Dimension(200, 20));
        AcctNameField.setPreferredSize(new java.awt.Dimension(200, 20));
        EntryPanel.add(AcctNameField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(EntryPanel, gridBagConstraints);

        DescriptionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        DescriptionPanel.setBackground(new java.awt.Color(190, 190, 226));
        DescriptionPanel.setMaximumSize(new java.awt.Dimension(325, 30));
        DescriptionPanel.setMinimumSize(new java.awt.Dimension(325, 30));
        DescriptionPanel.setPreferredSize(new java.awt.Dimension(325, 30));
        DescrLabel.setDisplayedMnemonic('D');
        DescrLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        DescrLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DescrLabel.setText("Description: ");
        DescrLabel.setPreferredSize(new java.awt.Dimension(115, 15));
        DescriptionPanel.add(DescrLabel);

        DescrField.setMaximumSize(new java.awt.Dimension(200, 20));
        DescrField.setMinimumSize(new java.awt.Dimension(200, 20));
        DescrField.setPreferredSize(new java.awt.Dimension(200, 20));
        DescriptionPanel.add(DescrField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(DescriptionPanel, gridBagConstraints);

        HostPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        HostPanel.setBackground(new java.awt.Color(190, 190, 226));
        HostPanel.setMaximumSize(new java.awt.Dimension(325, 30));
        HostPanel.setMinimumSize(new java.awt.Dimension(325, 30));
        HostPanel.setPreferredSize(new java.awt.Dimension(325, 30));
        HostLabel.setDisplayedMnemonic('H');
        HostLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        HostLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        HostLabel.setText("Host: ");
        HostLabel.setPreferredSize(new java.awt.Dimension(115, 15));
        HostLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        HostPanel.add(HostLabel);

        HostField.setMaximumSize(new java.awt.Dimension(200, 20));
        HostField.setMinimumSize(new java.awt.Dimension(200, 20));
        HostField.setPreferredSize(new java.awt.Dimension(200, 20));
        HostPanel.add(HostField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel1.add(HostPanel, gridBagConstraints);

        IconPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        IconPanel.setBackground(new java.awt.Color(190, 190, 226));
        IconPanel.setMaximumSize(new java.awt.Dimension(135, 60));
        IconPanel.setMinimumSize(new java.awt.Dimension(135, 60));
        IconPanel.setPreferredSize(new java.awt.Dimension(135, 60));
        jLabel4.setBackground(new java.awt.Color(190, 190, 226));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iWork_enabled.png")));
        jLabel4.setMaximumSize(new java.awt.Dimension(200, 50));
        jLabel4.setMinimumSize(new java.awt.Dimension(200, 50));
        jLabel4.setPreferredSize(new java.awt.Dimension(200, 50));
        IconPanel.add(jLabel4);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(IconPanel, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(190, 190, 226));
        jTabbedPane1.setBackground(new java.awt.Color(190, 190, 226));
        jTabbedPane1.setFont(new java.awt.Font("Dialog", 0, 12));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(175, 75));
        AuthTab.setLayout(new java.awt.GridBagLayout());

        AuthTab.setBackground(new java.awt.Color(190, 190, 226));
        AuthTab.setMaximumSize(new java.awt.Dimension(0, 0));
        AuthTab.setMinimumSize(new java.awt.Dimension(0, 0));
        AuthTab.setPreferredSize(new java.awt.Dimension(0, 200));
        GroupAuthPanel.setLayout(new java.awt.GridBagLayout());

        GroupAuthPanel.setBackground(new java.awt.Color(190, 190, 226));
        GroupAuthButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        GroupAuthButtonPanel.setBackground(new java.awt.Color(190, 190, 226));
        GroupAuthButton.setBackground(new java.awt.Color(190, 190, 226));
        GroupAuthButton.setFont(new java.awt.Font("Dialog", 0, 12));
        GroupAuthButton.setSelected(true);
        GroupAuthButton.setText("Group Authentication");
        GroupAuthButtonPanel.add(GroupAuthButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        GroupAuthPanel.add(GroupAuthButtonPanel, gridBagConstraints);

        GroupNamePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        GroupNamePanel.setBackground(new java.awt.Color(190, 190, 226));
        GroupNamePanel.setMaximumSize(new java.awt.Dimension(425, 30));
        GroupNamePanel.setMinimumSize(new java.awt.Dimension(425, 30));
        GroupNamePanel.setPreferredSize(new java.awt.Dimension(425, 30));
        GroupLabel.setDisplayedMnemonic('C');
        GroupLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        GroupLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        GroupLabel.setText("Group Name: ");
        GroupLabel.setPreferredSize(new java.awt.Dimension(115, 15));
        GroupNamePanel.add(GroupLabel);

        GroupField.setEditable(false);
        GroupField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        GroupField.setText("VPN nextGen");
        GroupField.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        GroupField.setMargin(new java.awt.Insets(0, 25, 0, 0));
        GroupField.setMaximumSize(new java.awt.Dimension(300, 25));
        GroupField.setMinimumSize(new java.awt.Dimension(300, 25));
        GroupField.setPreferredSize(new java.awt.Dimension(300, 25));
        GroupField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GroupFieldActionPerformed(evt);
            }
        });

        GroupNamePanel.add(GroupField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        GroupAuthPanel.add(GroupNamePanel, gridBagConstraints);

        GroupPasswdPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        GroupPasswdPanel.setBackground(new java.awt.Color(190, 190, 226));
        GroupPasswdPanel.setMaximumSize(new java.awt.Dimension(425, 30));
        GroupPasswdPanel.setMinimumSize(new java.awt.Dimension(425, 30));
        GroupPasswdPanel.setPreferredSize(new java.awt.Dimension(425, 30));
        GroupPasswdLabel.setDisplayedMnemonic('D');
        GroupPasswdLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        GroupPasswdLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        GroupPasswdLabel.setText("Password: ");
        GroupPasswdLabel.setPreferredSize(new java.awt.Dimension(115, 15));
        GroupPasswdPanel.add(GroupPasswdLabel);

        GroupPasswdField.setEditable(false);
        GroupPasswdField.setText("vpn4sun");
        GroupPasswdField.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        GroupPasswdField.setMaximumSize(new java.awt.Dimension(300, 25));
        GroupPasswdField.setMinimumSize(new java.awt.Dimension(300, 25));
        GroupPasswdField.setPreferredSize(new java.awt.Dimension(300, 25));
        GroupPasswdPanel.add(GroupPasswdField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        GroupAuthPanel.add(GroupPasswdPanel, gridBagConstraints);

        GroupPassConfirmPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        GroupPassConfirmPanel.setBackground(new java.awt.Color(190, 190, 226));
        GroupPassConfirmPanel.setMaximumSize(new java.awt.Dimension(425, 30));
        GroupPassConfirmPanel.setMinimumSize(new java.awt.Dimension(425, 30));
        GroupPassConfirmPanel.setPreferredSize(new java.awt.Dimension(425, 30));
        GroupPassConfirmLabel.setDisplayedMnemonic('H');
        GroupPassConfirmLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        GroupPassConfirmLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        GroupPassConfirmLabel.setText("Confirm Password: ");
        GroupPassConfirmLabel.setPreferredSize(new java.awt.Dimension(115, 15));
        GroupPassConfirmLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        GroupPassConfirmPanel.add(GroupPassConfirmLabel);

        GroupPassConfirmField.setEditable(false);
        GroupPassConfirmField.setText("vpn4sun");
        GroupPassConfirmField.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        GroupPassConfirmField.setMaximumSize(new java.awt.Dimension(300, 25));
        GroupPassConfirmField.setMinimumSize(new java.awt.Dimension(300, 25));
        GroupPassConfirmField.setPreferredSize(new java.awt.Dimension(300, 25));
        GroupPassConfirmPanel.add(GroupPassConfirmField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        GroupAuthPanel.add(GroupPassConfirmPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        AuthTab.add(GroupAuthPanel, gridBagConstraints);

        GroupCertPanel.setLayout(new java.awt.GridBagLayout());

        GroupCertPanel.setBackground(new java.awt.Color(190, 190, 226));
        GroupAuthButtonPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        GroupAuthButtonPanel1.setBackground(new java.awt.Color(190, 190, 226));
        CertAuthButton.setBackground(new java.awt.Color(190, 190, 226));
        CertAuthButton.setFont(new java.awt.Font("Dialog", 0, 12));
        CertAuthButton.setText("Certificate Authentication");
        CertAuthButton.setEnabled(false);
        GroupAuthButtonPanel1.add(CertAuthButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        GroupCertPanel.add(GroupAuthButtonPanel1, gridBagConstraints);

        GroupCertNamePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        GroupCertNamePanel.setBackground(new java.awt.Color(190, 190, 226));
        GroupCertNamePanel.setMaximumSize(new java.awt.Dimension(325, 20));
        GroupCertNamePanel.setMinimumSize(new java.awt.Dimension(325, 20));
        GroupCertNamePanel.setPreferredSize(new java.awt.Dimension(325, 20));
        CertNameLabel.setDisplayedMnemonic('C');
        CertNameLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        CertNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        CertNameLabel.setText("Name: ");
        CertNameLabel.setMaximumSize(new java.awt.Dimension(50, 15));
        CertNameLabel.setMinimumSize(new java.awt.Dimension(50, 15));
        CertNameLabel.setPreferredSize(new java.awt.Dimension(50, 15));
        GroupCertNamePanel.add(CertNameLabel);

        CertNameCombo.setFont(new java.awt.Font("Dialog", 0, 12));
        CertNameCombo.setMaximumSize(new java.awt.Dimension(200, 20));
        CertNameCombo.setMinimumSize(new java.awt.Dimension(200, 20));
        CertNameCombo.setPreferredSize(new java.awt.Dimension(200, 20));
        CertNameCombo.setEnabled(false);
        GroupCertNamePanel.add(CertNameCombo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        GroupCertPanel.add(GroupCertNamePanel, gridBagConstraints);

        CertChainPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        CertChainPanel.setBackground(new java.awt.Color(190, 190, 226));
        CertChainPanel.setMaximumSize(new java.awt.Dimension(325, 20));
        CertChainPanel.setMinimumSize(new java.awt.Dimension(325, 20));
        CertChainPanel.setPreferredSize(new java.awt.Dimension(325, 20));
        CertChainCheckBox.setBackground(new java.awt.Color(190, 190, 226));
        CertChainCheckBox.setFont(new java.awt.Font("Dialog", 0, 12));
        CertChainCheckBox.setText("Send CA Certificate Chain");
        CertChainCheckBox.setEnabled(false);
        CertChainPanel.add(CertChainCheckBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        GroupCertPanel.add(CertChainPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        AuthTab.add(GroupCertPanel, gridBagConstraints);

        jTabbedPane1.addTab("Authentication", AuthTab);

        TransportTab.setLayout(new java.awt.GridBagLayout());

        TransportTab.setBackground(new java.awt.Color(190, 190, 226));
        ProtocolPanel.setLayout(new java.awt.GridBagLayout());

        ProtocolPanel.setBackground(new java.awt.Color(190, 190, 226));
        TunnelingPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        TunnelingPanel.setBackground(new java.awt.Color(190, 190, 226));
        TransparentTunneling.setBackground(new java.awt.Color(190, 190, 226));
        TransparentTunneling.setSelected(true);
        TransparentTunneling.setText("Enable Transparent Tunneling");
        TunnelingPanel.add(TransparentTunneling);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        ProtocolPanel.add(TunnelingPanel, gridBagConstraints);

        IPSecUDPPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        IPSecUDPPanel.setBackground(new java.awt.Color(190, 190, 226));
        IPSecUDPPanel.setMaximumSize(new java.awt.Dimension(425, 25));
        IPSecUDPPanel.setMinimumSize(new java.awt.Dimension(425, 25));
        IPSecUDPPanel.setPreferredSize(new java.awt.Dimension(425, 25));
        IPSecUDPButton.setBackground(new java.awt.Color(190, 190, 226));
        IPSecUDPButton.setFont(new java.awt.Font("Dialog", 0, 12));
        IPSecUDPButton.setSelected(true);
        IPSecUDPButton.setText("IPSec over UDP ( NAT / PAT )");
        IPSecUDPButton.setMaximumSize(new java.awt.Dimension(202, 20));
        IPSecUDPButton.setMinimumSize(new java.awt.Dimension(202, 20));
        IPSecUDPButton.setPreferredSize(new java.awt.Dimension(202, 20));
        IPSecUDPPanel.add(IPSecUDPButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 25, 3, 0);
        ProtocolPanel.add(IPSecUDPPanel, gridBagConstraints);

        IPSecTCPPanel.setLayout(new java.awt.GridBagLayout());

        IPSecTCPPanel.setBackground(new java.awt.Color(190, 190, 226));
        IPSecTCPPanel.setMaximumSize(new java.awt.Dimension(425, 25));
        IPSecTCPPanel.setMinimumSize(new java.awt.Dimension(425, 25));
        IPSecTCPPanel.setPreferredSize(new java.awt.Dimension(425, 25));
        IPSecTCPButton.setBackground(new java.awt.Color(190, 190, 226));
        IPSecTCPButton.setFont(new java.awt.Font("Dialog", 0, 12));
        IPSecTCPButton.setText("IPSec over TCP");
        IPSecTCPButton.setMaximumSize(new java.awt.Dimension(120, 25));
        IPSecTCPButton.setMinimumSize(new java.awt.Dimension(120, 25));
        IPSecTCPButton.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        IPSecTCPPanel.add(IPSecTCPButton, gridBagConstraints);

        TcpPortLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        TcpPortLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        TcpPortLabel.setText("TCP Port:");
        TcpPortLabel.setEnabled(false);
        TcpPortLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.insets = new java.awt.Insets(5, 30, 2, 0);
        IPSecTCPPanel.add(TcpPortLabel, gridBagConstraints);

        TcpPortField.setBackground(new java.awt.Color(190, 190, 224));
        TcpPortField.setEditable(false);
        TcpPortField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TcpPortField.setText("10000");
        TcpPortField.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        TcpPortField.setMaximumSize(new java.awt.Dimension(50, 20));
        TcpPortField.setMinimumSize(new java.awt.Dimension(50, 20));
        TcpPortField.setPreferredSize(new java.awt.Dimension(50, 20));
        TcpPortField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 155);
        IPSecTCPPanel.add(TcpPortField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 25, 3, 0);
        ProtocolPanel.add(IPSecTCPPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 34, 0);
        TransportTab.add(ProtocolPanel, gridBagConstraints);

        NATPanel.setLayout(new java.awt.GridBagLayout());

        NATPanel.setBackground(new java.awt.Color(190, 190, 226));
        LanAccessPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        LanAccessPanel.setBackground(new java.awt.Color(190, 190, 226));
        LocalLanButton.setBackground(new java.awt.Color(190, 190, 226));
        LocalLanButton.setFont(new java.awt.Font("Dialog", 0, 12));
        LocalLanButton.setText("Allow Local LAN Access");
        LanAccessPanel.add(LocalLanButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        NATPanel.add(LanAccessPanel, gridBagConstraints);

        PeerTimoutPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        PeerTimoutPanel.setBackground(new java.awt.Color(190, 190, 226));
        PeerTimoutPanel.setMaximumSize(new java.awt.Dimension(325, 20));
        PeerTimoutPanel.setMinimumSize(new java.awt.Dimension(325, 20));
        PeerTimoutPanel.setPreferredSize(new java.awt.Dimension(325, 20));
        PeerTimeoutLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        PeerTimeoutLabel.setText("Peer response timeout (seconds): ");
        PeerTimoutPanel.add(PeerTimeoutLabel);

        PeerTimeoutField.setText("90");
        PeerTimeoutField.setMaximumSize(new java.awt.Dimension(40, 20));
        PeerTimeoutField.setMinimumSize(new java.awt.Dimension(40, 20));
        PeerTimeoutField.setPreferredSize(new java.awt.Dimension(40, 20));
        PeerTimoutPanel.add(PeerTimeoutField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        NATPanel.add(PeerTimoutPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        TransportTab.add(NATPanel, gridBagConstraints);

        jTabbedPane1.addTab("Transport", TransportTab);

        jTabbedPane1.addTab("Backup Servers", BackupServerTab);

        jPanel2.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jPanel3.setBackground(new java.awt.Color(190, 190, 226));
        SaveButton.setBackground(new java.awt.Color(190, 190, 226));
        SaveButton.setFont(new java.awt.Font("Dialog", 0, 12));
        SaveButton.setText("Save");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAccount(evt);
            }
        });

        jPanel3.add(SaveButton);

        CancelButton.setBackground(new java.awt.Color(190, 190, 226));
        CancelButton.setFont(new java.awt.Font("Dialog", 0, 12));
        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel(evt);
            }
        });

        jPanel3.add(CancelButton);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void Cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_Cancel
    private boolean checkPassword ( char[] pass, char[] confirm ){
        
        if ( pass.length != confirm.length ) return false;
        
        for ( int i=0; i<pass.length; i++ ){
            if ( pass[i] != confirm[i] ) return false;
        }
        
        
       return true; 
    }
    protected void createAccount(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAccount
        // Add your handling code here:
        // get all info from fields/buttons
        //
        
        String ProfileDir = this.UI.getDefaultProfileDir();
        
        Hashtable ht = new Hashtable();
        if ( this.UI.accountExists(AcctNameField.getText()) ){
             JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Connection Entry " + AcctNameField.getText() + " already taken" );
             return;
        }
        ht.put( ProfileFields.ProfileName,  AcctNameField.getText() );
        
        ht.put( ProfileFields.Description, DescrField.getText() );
        ht.put( ProfileFields.Host,  HostField.getText() );
        
        ht.put( ProfileFields.GroupName, GroupField.getText() );
        
        char[] groupPass = GroupPasswdField.getPassword();
        char[] groupConfirmPass = GroupPassConfirmField.getPassword();
        
      
        //if ( groupPass.equals(groupConfirmPass) ){
            
        if ( checkPassword(groupPass, groupConfirmPass) ){
            ht.put( ProfileFields.GroupPwd,  decodePasswd( groupPass ) );
        } else {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Group Password and Confirm Group Password don't match" );
            return;
        }
        
        ht.put( ProfileFields.TCPTunnelingPort,  TcpPortField.getText() );
        
        ht.put( ProfileFields.PeerTimeout,  PeerTimeoutField.getText() );
        
        if ( GroupAuthButton.isSelected() ){
            // use group authentication
        } else if ( CertAuthButton.isSelected() ){
            // use cert authentication
        }
        
        if ( TransparentTunneling.isSelected() ){
            ht.put( ProfileFields.EnableNat,  "1" );
        } else {
             ht.put( ProfileFields.EnableNat,  "0" );
        }
        
        if ( LocalLanButton.isSelected() ){
            ht.put( ProfileFields.EnableLocalLAN, "1" );
        } else {
            ht.put( ProfileFields.EnableLocalLAN, "0" );
        }
        
        String CertName = (String)CertNameCombo.getSelectedItem();
        
        if ( IPSecUDPButton.isSelected() ){
            ht.put( ProfileFields.TunnelingMode, "0" );
        } else {
            ht.put( ProfileFields.TunnelingMode, "1" );
        }
        
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
         
         
         *
         *
         *
         *
         *
         **/
        
        
        // New account, make sure acctname not taken.
        
        SaveAccount(ht, ProfileDir);
        
    }//GEN-LAST:event_createAccount

    private void GroupFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GroupFieldActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_GroupFieldActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new NewAccount(new javax.swing.JFrame(), true).show();
    }
    
    public int SaveAccount(java.util.Hashtable accountInfo, java.lang.String dir) {
        // get values, save to file
        
        Hashtable acct = accountInfo;
        Enumeration en = acct.keys();
        while ( en.hasMoreElements() ){
            Object key = en.nextElement();
            System.out.println( key + "=" + acct.get( key ) );
        }
        // add to existing accounts
        UI.addAccount(AcctNameField.getText(), accountInfo);
        //
        this.dispose();
        return 0;
    }    
    
    public String decodePasswd(char[] passwd) {
        StringBuffer buf = new StringBuffer();
        
        for ( int i=0; i<passwd.length; i++ ){
                buf.append(passwd[i]);
        }
        
        return buf.toString();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AcctLabel;
    protected javax.swing.JTextField AcctNameField;
    private javax.swing.ButtonGroup AuthButtonGroup;
    private javax.swing.JPanel AuthTab;
    private javax.swing.JPanel BackupServerTab;
    protected javax.swing.JButton CancelButton;
    private javax.swing.JRadioButton CertAuthButton;
    private javax.swing.JCheckBox CertChainCheckBox;
    private javax.swing.JPanel CertChainPanel;
    private javax.swing.JComboBox CertNameCombo;
    private javax.swing.JLabel CertNameLabel;
    protected javax.swing.JTextField DescrField;
    private javax.swing.JLabel DescrLabel;
    private javax.swing.JPanel DescriptionPanel;
    private javax.swing.JPanel EntryPanel;
    protected javax.swing.JRadioButton GroupAuthButton;
    private javax.swing.JPanel GroupAuthButtonPanel;
    private javax.swing.JPanel GroupAuthButtonPanel1;
    private javax.swing.JPanel GroupAuthPanel;
    private javax.swing.JPanel GroupCertNamePanel;
    private javax.swing.JPanel GroupCertPanel;
    private javax.swing.JTextField GroupField;
    private javax.swing.JLabel GroupLabel;
    private javax.swing.JPanel GroupNamePanel;
    private javax.swing.JPasswordField GroupPassConfirmField;
    private javax.swing.JLabel GroupPassConfirmLabel;
    private javax.swing.JPanel GroupPassConfirmPanel;
    private javax.swing.JPasswordField GroupPasswdField;
    private javax.swing.JLabel GroupPasswdLabel;
    private javax.swing.JPanel GroupPasswdPanel;
    protected javax.swing.JTextField HostField;
    private javax.swing.JLabel HostLabel;
    private javax.swing.JPanel HostPanel;
    private javax.swing.ButtonGroup IPSecButtonGroup;
    protected javax.swing.JRadioButton IPSecTCPButton;
    private javax.swing.JPanel IPSecTCPPanel;
    protected javax.swing.JRadioButton IPSecUDPButton;
    private javax.swing.JPanel IPSecUDPPanel;
    private javax.swing.JPanel IconPanel;
    private javax.swing.JPanel LanAccessPanel;
    protected javax.swing.JCheckBox LocalLanButton;
    private javax.swing.JPanel NATPanel;
    protected javax.swing.JTextField PeerTimeoutField;
    private javax.swing.JLabel PeerTimeoutLabel;
    private javax.swing.JPanel PeerTimoutPanel;
    private javax.swing.JPanel ProtocolPanel;
    protected javax.swing.JButton SaveButton;
    private javax.swing.JTextField TcpPortField;
    private javax.swing.JLabel TcpPortLabel;
    protected javax.swing.JCheckBox TransparentTunneling;
    private javax.swing.JPanel TransportTab;
    private javax.swing.JPanel TunnelingPanel;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
    protected vpn3k UI;
    protected String acctName;
    
    
}   