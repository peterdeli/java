/*
 * vpn3k.java
 *
 * Created on October 13, 2003, 4:36 PM
 */

/**
 *
 *
 *private void accountSelected(java.awt.event.MouseEvent evt) {
 * // Add your handling code here:
 * TableModel tm = jTable1.getModel();
 *
 * jTable1.setColumnSelectionAllowed(false);
 * jTable1.setRowSelectionAllowed(true);
 * //jTable1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
 * int row = jTable1.getSelectedRow();
 * System.out.println( "Row selected: " + row );
 * System.out.println("Account name: " + tm.getValueAt(row, 0));
 *
 * jTable1.setRowSelectionInterval(row,row);
 * //tm.setValueAt(new String("row " + row ), row,0);
 * //tm.setValueAt(new String("Column 1"), row,1);
 * //tm.setValueAt(new String("Column 2"), row,2);
 *
 * //System.out.println ( "Button clicked at row " + tm.getRowCount() );
 * //return tm.getValueAt(row, 0);
 * }
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
import java.net.*;


public class vpn3k extends javax.swing.JFrame   {
    
    /** Creates new form vpn3k */
    public vpn3k() {
        checkVpnFiles();
        setIconImage(minimizeIcon.getImage());
        new vpnSplash(this,5000);
        //javaVersion();
        //bongo
        setLookAndFeel();
        setInterpreterPath();
        initComponents();
        centerWindow();
        loadConfigs();
        loadInitFile();
        initColumns();
        this.setLocationRelativeTo(null);
        DetachedLogWin = new LogWindow(this);
        SimpleWin = new vpn3kSimple(this);
        setStartupMode();
        setView( StartupMode );
        
        StatWindow = new StatsWindow( this );
        
    }
    private void checkVpnFiles(){
        
        Vector files = new Vector();
        Vector missingFiles = new Vector();
        files.add( ConfigDir );
        files.add( ProfilesDir );
        files.add( InitFile );
        files.add( vpnclientBinary );
        boolean missing = false;
        for ( Enumeration en = files.elements(); en.hasMoreElements(); ){
            String fileName = (String)en.nextElement();
            File f = new File( fileName );
            if ( ! f.exists() ){
                missing = true;
                missingFiles.add(fileName);
            }
        }
        if ( missing ){
            StringBuffer sb = new StringBuffer();
            for ( Enumeration en = missingFiles.elements(); en.hasMoreElements(); ){
                sb.append( (String)en.nextElement() + "\n" );
            }
            
            String message = new String( "Missing files need for proper operation:\n" + sb.toString() + " Please contact your administrator" );
            JOptionPane.showMessageDialog(null, message, "VPN Tool | Missing Files", JOptionPane.ERROR_MESSAGE );
            //System.exit(1);
        }
    }
    private void setInterpreterPath(){
        try{
            String expect_path = System.getProperty("EXPECT_PATH");
            //System.out.println( "EXPECT_PATH = " + System.getProperty("EXPECT_PATH") + "\nexpect_path: " + expect_path );
            //System.out.println( "expect_path length: " + expect_path.length() );
            if ( expect_path.length() < 1  ){
                System.setProperty( "EXPECT_PATH", getInterpreter() );
            }
            System.out.println( "Expect interpreter: " + System.getProperty( "EXPECT_PATH" ));
            return;
        } catch ( java.lang.NullPointerException np ){
            System.out.println( "No EXPECT_PATH property: " + np.getLocalizedMessage() );
        }
		if ( getInterpreter() != null ){
			System.setProperty( "EXPECT_PATH", getInterpreter() );
			System.out.println( "Expect interpreter: " + System.getProperty( "EXPECT_PATH" ));
		}
    }
    
    public String getInterpreter(){
        
        String interpreter = null;
        
        if (osName.equals("SunOS")){
            interpreter = "/opt/sfw/bin/expect";
        } else if ( osName.equals( "Linux" )){
            interpreter = "/usr/bin/expect";
        } else {
            JOptionPane.showMessageDialog(null,
            "No expect interpreter found", "vpn3k | No expect interpreter found",
            JOptionPane.ERROR_MESSAGE );
			return null;
		}
		File interp = new File( interpreter );
        boolean exists = interp.exists();
        System.out.println( "File " + interpreter + " exists: " + exists );
        while ( ! exists ){
            JOptionPane.showMessageDialog(null,
            "No expect interpreter found", "vpn3k | No expect interpreter found",
            JOptionPane.ERROR_MESSAGE );
            try{
                interpreter = JOptionPane.showInputDialog(null, "No expect interpreter found, please enter path to expect, or leave blank to exit:");
                if ( interpreter.length() < 1 ) System.exit( 1 );
            } catch ( java.lang.NullPointerException np ){
                System.exit( 1 );
            }
            interp = new File( interpreter );
            exists = interp.exists();
            System.setProperty("EXPECT_PATH", interpreter );
            
        }
        return interpreter;
    }
    
    protected void setStartupMode(){
        editInitData(InitHeadings.GUI,  InitFields.ADVANCED_VIEW,  String.valueOf(StartupMode) );
        
    }
    protected void setView( int type ){
        
        // 0 = simple, 1 = advanced
        if ( type == 0 ){
            if ( this.isVisible() == true ) {
                this.setVisible(false);
            }
            if ( this.SimpleWin.isVisible() == false ){
                int index = this.getSelectedAccountRow();
                this.SimpleWin.AccountCombo.setSelectedIndex(this.getSelectedAccountRow());
                this.SimpleWin.setLocation(this.getLocation());
                this.SimpleWin.setVisible(true);
                this.SimpleWin.repaint();
            }
        }else{
            if ( this.SimpleWin.isVisible() == true ) {
                this.SimpleWin.setVisible(false);
            }
            if ( this.isVisible() == false ){
                this.setLocation(this.getLocation());
                this.setVisible(true);
                this.repaint();
            }
        }
    }
    public void centerWindow(){
        java.awt.Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int width = this.getWidth();
        int height = this.getHeight();
        
        int x = ( dim.width/2 - width/2 );
        int y = ( dim.height/2 - height/2 );
        
        this.setLocation( x, y );
        
    }
    
    protected void javaVersion() throws java.lang.NullPointerException {
        
        final int MAJOR=1;
        final int MINOR=4;
        final int RELEASE=0;
        
        String version = System.getProperty("java.version");
        
        
        if ( version == null ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), new String( "Java version " + version + " is incorrect" ), "Java Error", JOptionPane.OK_OPTION);
            System.exit(1);
        }
        
        if ( version.compareTo( "1.4.0" ) >= 0 ) return;
        
        
        String[] major_minor = version.split("_", 2);
        
        if ( major_minor[0] == null ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), new String( "Java version " + version + " is incorrect" ), "Java Error", JOptionPane.OK_OPTION);
            System.exit(1);
        }
        // 1.4.2 // 01,02, etc
        String[] majorValue = major_minor[0].split( "\\.", 3 );
        
        if ( majorValue[0] == null ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), new String( "Java version " + version + " is incorrect" ), "Java Error", JOptionPane.OK_OPTION);
            System.exit(1);
        }
        
        int major = new Integer( majorValue[0] ).intValue();
        int minor=-1;
        int release=-1;
        
        if ( majorValue[1] != null ){
            minor = new Integer( majorValue[1] ).intValue();
        }
        
        if ( majorValue[2] != null ){
            if ( majorValue[2].length() > 1 ){
                String subMajor = majorValue[2].substring(0,1);
                release = new Integer( subMajor ).intValue();
            } else {
                release = new Integer( majorValue[2] ).intValue();
            }
        }
        // [0] >= 1, [1] >= 4, [2] >= 2
        
        
        
        if ( major  < MAJOR  ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), new String( "Java version " + version + " is too old" ), "Java Error", JOptionPane.OK_OPTION);
            System.exit(1);
        } else if (minor  < MINOR ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), new String( "Java version " + version + " is too old" ), "Java Error", JOptionPane.OK_OPTION);
            System.exit(1);
        } else if (release < RELEASE ) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), new String( "Java version " + version + " is too old" ), "Java Error", JOptionPane.OK_OPTION);
            System.exit(1);
        }
        
        System.out.println( "Java Version: " + version );
        
    }
    protected void setLookAndFeel(){
        
        try {
            
            UIManager.LookAndFeelInfo[] lafInfo = javax.swing.UIManager.getInstalledLookAndFeels();
            if (osName != null && osName.toLowerCase().indexOf("linux") != -1) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            }
            // for ( int i=0; i<lafInfo.length; i++ ){
            //    System.out.println( "Installed LAF: " + lafInfo[i].getName() );
            // }
            System.out.println( "Using: " + UIManager.getLookAndFeel().getDescription() );
        }catch ( Exception x ){
            x.printStackTrace();
        }
    }
    protected void initColumns(){
        JTextField dummyField = new JTextField();
        
        TableColumn ColumnA = jTable1.getColumn(jTable1.getColumnName(0));
        ColumnA.setPreferredWidth(150);
        //ColumnA.setPreferredWidth(jTable1.getColumnName(0).length());
        ColumnA.setHeaderRenderer( new vpn3k.centeredHeader( jTable1.getColumnName(0)));
        ColumnA.setCellRenderer(new vpn3k.LabelRenderer());
        ColumnA.setCellEditor(new vpn3k.columnEditor(dummyField));
        
        TableColumn ColumnB = jTable1.getColumn(jTable1.getColumnName(1));
        ColumnB.setPreferredWidth(175);
        //ColumnB.setWidth( ( jTable1.getColumnName(1).length() * 8) );
        ColumnB.setHeaderRenderer( new vpn3k.centeredHeader( jTable1.getColumnName(1)));
        ColumnB.setCellRenderer(new vpn3k.LabelRenderer());
        ColumnB.setCellEditor(new vpn3k.columnEditor(dummyField));
        
        TableColumn ColumnC = jTable1.getColumn(jTable1.getColumnName(2));
        //ColumnC.setPreferredWidth(jTable1.getColumnName(2).length());
        //ColumnC.setMaxWidth( 110 );
        //ColumnC.setMinWidth( 110 );
        ColumnC.setPreferredWidth( 150 );
        ColumnC.setHeaderRenderer( new vpn3k.centeredHeader( jTable1.getColumnName(2)));
        ColumnC.setCellRenderer(new vpn3k.LabelRenderer());
        ColumnC.setCellEditor(new vpn3k.columnEditor(dummyField));
        
        TableColumn ColumnD = jTable1.getColumn(jTable1.getColumnName(3));
        //ColumnD.setMaxWidth( 75 );
        //  ColumnD.setMinWidth( 75 );
        //ColumnD.setPreferredWidth( 75 );
        ColumnD.setHeaderRenderer( new vpn3k.centeredHeader( jTable1.getColumnName(3)));
        ColumnD.setCellRenderer(new vpn3k.LabelRenderer());
        ColumnD.setCellEditor(new vpn3k.columnEditor(dummyField));
        
        
        
    }
    
    
    class centeredHeader extends DefaultTableCellRenderer{
        
        public centeredHeader(String title){
            setHorizontalAlignment(JLabel.CENTER);
            //setBorder( BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            //setBackground( new Color ( 204,204,204  ));
            //LookAndFeel.installColorsAndFont(this, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
            //LookAndFeel.installBorder(this, "TableHeader.cellBorder" );
            
        }
        public void updateUI(){
            super.updateUI();
            
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col ){
            
            setHorizontalAlignment(JLabel.CENTER);
            
            setBorder( BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            setBorder( BorderFactory.createEtchedBorder());
            setText((String)value);
            //LookAndFeel.installColorsAndFont(this, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
            //LookAndFeel.installBorder(this, "TableHeader.cellBorder" );
            return (Component)this;
        }
        
        
        public void setValue(Object value){
            
            setHorizontalAlignment(JLabel.CENTER);
            setText((String)value);
        }
    }
    
    class columnEditor extends DefaultCellEditor{
        public columnEditor( javax.swing.JTextField txt ){
            super(txt);
            
        }
        public boolean isCellEditable(EventObject e){
            return false;
        }
    }
    
    protected void setDefaultRow(int row){
        this.DefaultRow = row;
    }
    
    protected int getDefaultRow(){
        return this.DefaultRow ;
    }
    
    class LabelRenderer extends DefaultTableCellRenderer{
        
        public LabelRenderer(){
            setHorizontalAlignment(JLabel.CENTER);
            //setBackground(new java.awt.Color(190, 190, 226));
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col ){
            
            DefaultTableModel tm = (DefaultTableModel)table.getModel();
            
            if ( (col == 0)  ){
                String defaultAcct = getDefaultAccountName();
                String currAcct = (String)value;
                if ( defaultAcct != null ){
                    if (currAcct.compareTo(defaultAcct) == 0 ){
                        setDefaultRow(row);
                    }
                }
            }
            
            /*
             
            String acctName = (String)tm.getValueAt(row, 0);
            String defaultAcct = getDefaultAccountName();
            if ( defaultAcct != null ){
                System.out.println( "row " + row + " col " + col + " : " + acctName );
                System.out.println( "defaultAcct: " + defaultAcct );
                if ( acctName.compareTo(defaultAcct) == 0 ){
             
                    setFont( new java.awt.Font("Dialog", Font.BOLD, 12 ));
                } else {
                    setFont( new java.awt.Font("Dialog", 0, 12 ));
                }
            }
             **/
            
            
            if ( row == getDefaultRow() ){
                setFont( new java.awt.Font("Dialog", Font.BOLD, 12 ));
            } else {
                setFont( new java.awt.Font("Dialog", 0, 12 ));
            }
            
            if ( isSelected ){
                //setHorizontalAlignment(JLabel.CENTER);
                setBackground( new Color(190,190,226) );
                setText((String)value);
                
            } else {
                //setHorizontalAlignment(JLabel.CENTER);
                setBackground( Color.white );
                setText((String)value);
            }
            return this;
        }
        
        
        public void setValue(Object value){
            
            setHorizontalAlignment(JLabel.CENTER);
            setText((String)value);
            
            
            //setBackground(new java.awt.Color(190, 190, 226));
            
        }
    }
    
    
    public void setDefaultAccountName( String name ){
        
        DefaultAccountName = name;
        // edit init file
        editInitData(
        InitHeadings.GUI,
        InitFields.DEFAULT_CONNECTION_ENTRY,
        DefaultAccountName
        );
        // write to InitFile
        writeInitFile();
        
    }
    public String getDefaultAccountName(){
        // check for .default file
        //
        return this.DefaultAccountName;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jFrame1 = new javax.swing.JFrame();
        jFrame1.setSize( 400,250);
        jPanel12 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jCheckBox3 = new javax.swing.JCheckBox();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup1.add( jRadioButton1);
        buttonGroup1.add( jRadioButton2 );

        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        EnableLogButton = new javax.swing.JButton();
        ClearLogButton = new javax.swing.JButton();
        LogSettingsButton = new javax.swing.JButton();
        ShowLogWinButton = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        ConnectButton = new javax.swing.JButton();
        NewButton = new javax.swing.JButton();
        ImportButton = new javax.swing.JButton();
        ModifyButton = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        LogWin = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        StatusField = new javax.swing.JLabel();
        ProgressPanel = new javax.swing.JPanel();
        jMenuBar2 = new javax.swing.JMenuBar();
        EntryMenu = new javax.swing.JMenu();
        menuConnect = new javax.swing.JMenuItem();
        menuDisconnect = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        menuModify = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        menuDelete = new javax.swing.JMenuItem();
        menuDuplicate = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        menuSetDefault = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        menuNew = new javax.swing.JMenuItem();
        menuImport = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        menuConnectionWindow = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenuItem();
        StatusMenu = new javax.swing.JMenu();
        menuStatistics = new javax.swing.JMenuItem();
        menuNotifications = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        menuResetStats = new javax.swing.JMenuItem();
        LogMenu = new javax.swing.JMenu();
        menuEnable = new javax.swing.JMenuItem();
        menuClear = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        menuLogSettings = new javax.swing.JMenuItem();
        menuLogWin = new javax.swing.JMenuItem();
        menuSearchLog = new javax.swing.JMenuItem();
        menuSaveLog = new javax.swing.JMenuItem();
        OptionsMenu = new javax.swing.JMenu();
        menuSimpleMode = new javax.swing.JMenuItem();
        menuPreferences = new javax.swing.JMenuItem();
        HelpMenu = new javax.swing.JMenu();
        menuAbout = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenuItem();

        jFrame1.setTitle("Login Properties");
        jPanel12.setLayout(new java.awt.GridLayout(2, 0));

        jPanel12.setBorder(new javax.swing.border.TitledBorder("Login Method"));
        jRadioButton1.setText("Shared Key");
        jPanel12.add(jRadioButton1);

        jRadioButton2.setText("Certificate");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jPanel12.add(jRadioButton2);

        jFrame1.getContentPane().add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel13.setLayout(new java.awt.GridLayout(3, 2, 2, 0));

        jLabel8.setText("Login Name");
        jPanel13.add(jLabel8);

        jTextField1.setText("jTextField1");
        jPanel13.add(jTextField1);

        jLabel9.setText("Primary VPN Server:");
        jPanel13.add(jLabel9);

        jTextField2.setText("jTextField1");
        jPanel13.add(jTextField2);

        jLabel10.setText("Secondary VPN Server:");
        jPanel13.add(jLabel10);

        jTextField3.setText("jTextField1");
        jPanel13.add(jTextField3);

        jFrame1.getContentPane().add(jPanel13, java.awt.BorderLayout.WEST);

        jPanel17.setLayout(new java.awt.GridLayout(3, 0));

        jButton10.setText("OK");
        jPanel17.add(jButton10);

        jButton11.setText("Cancel");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jPanel17.add(jButton11);

        jButton12.setText("Advanced");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jPanel17.add(jButton12);

        jFrame1.getContentPane().add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jCheckBox3.setText("Use NAT Transparency Mode");
        jCheckBox3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel18.add(jCheckBox3);

        jFrame1.getContentPane().add(jPanel18, java.awt.BorderLayout.SOUTH);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel5.setText("Not Connected");
        jPanel8.add(jLabel5);

        jPanel7.add(jPanel8);

        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        setTitle("ITvpntool VPN 3000 Client | Advanced Mode");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel16.setLayout(new java.awt.BorderLayout());

        jPanel16.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel16.setPreferredSize(new java.awt.Dimension(550, 500));
        jPanel16.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                repaintWin(evt);
            }
        });

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));

        jPanel4.setBackground(new java.awt.Color(190, 190, 226));
        jPanel4.setPreferredSize(new java.awt.Dimension(550, 90));
        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 15));

        jPanel15.setBackground(new java.awt.Color(190, 190, 226));
        EnableLogButton.setBackground(new java.awt.Color(190, 190, 226));
        EnableLogButton.setFont(new java.awt.Font("Dialog", 0, 11));
        EnableLogButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enable_log.png")));
        EnableLogButton.setText("Enable");
        EnableLogButton.setBorder(null);
        EnableLogButton.setBorderPainted(false);
        EnableLogButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enable_log_pressed.png")));
        EnableLogButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enable_log_pressed.png")));
        EnableLogButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        EnableLogButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        EnableLogButton.setMaximumSize(new java.awt.Dimension(65, 60));
        EnableLogButton.setMinimumSize(new java.awt.Dimension(65, 60));
        EnableLogButton.setPreferredSize(new java.awt.Dimension(65, 60));
        EnableLogButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enable_log_pressed.png")));
        EnableLogButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enable_log_pressed.png")));
        EnableLogButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        EnableLogButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        EnableLogButton.setEnabled(false);
        EnableLogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnableLogButtonActionPerformed(evt);
            }
        });

        jPanel15.add(EnableLogButton);

        ClearLogButton.setBackground(new java.awt.Color(190, 190, 226));
        ClearLogButton.setFont(new java.awt.Font("Dialog", 0, 11));
        ClearLogButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear_log.png")));
        ClearLogButton.setText("Clear");
        ClearLogButton.setToolTipText("Clear Log Wndow");
        ClearLogButton.setBorder(null);
        ClearLogButton.setBorderPainted(false);
        ClearLogButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear_log_pressed.png")));
        ClearLogButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear_log_pressed.png")));
        ClearLogButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ClearLogButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ClearLogButton.setMaximumSize(new java.awt.Dimension(65, 60));
        ClearLogButton.setMinimumSize(new java.awt.Dimension(65, 60));
        ClearLogButton.setPreferredSize(new java.awt.Dimension(65, 60));
        ClearLogButton.setPressedIcon(new javax.swing.ImageIcon("/images/clear_log_pressed.png"));
        ClearLogButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear_log_pressed.png")));
        ClearLogButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ClearLogButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ClearLogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearLogWin(evt);
            }
        });

        jPanel15.add(ClearLogButton);

        LogSettingsButton.setBackground(new java.awt.Color(190, 190, 226));
        LogSettingsButton.setFont(new java.awt.Font("Dialog", 0, 11));
        LogSettingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/options_log.png")));
        LogSettingsButton.setText("Log Settings");
        LogSettingsButton.setBorder(null);
        LogSettingsButton.setBorderPainted(false);
        LogSettingsButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/images/options_log_pressed.png")));
        LogSettingsButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/options_log_pressed.png")));
        LogSettingsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        LogSettingsButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        LogSettingsButton.setMaximumSize(new java.awt.Dimension(80, 60));
        LogSettingsButton.setMinimumSize(new java.awt.Dimension(80, 60));
        LogSettingsButton.setPreferredSize(new java.awt.Dimension(80, 60));
        LogSettingsButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/options_log_pressed.png")));
        LogSettingsButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/options_log_pressed.png")));
        LogSettingsButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        LogSettingsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        LogSettingsButton.setEnabled(false);
        jPanel15.add(LogSettingsButton);

        ShowLogWinButton.setBackground(new java.awt.Color(190, 190, 226));
        ShowLogWinButton.setFont(new java.awt.Font("Dialog", 0, 11));
        ShowLogWinButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/show_log.png")));
        ShowLogWinButton.setText("Log Window");
        ShowLogWinButton.setToolTipText("Show Log Window");
        ShowLogWinButton.setBorder(null);
        ShowLogWinButton.setBorderPainted(false);
        ShowLogWinButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/images/show_log_pressed.png")));
        ShowLogWinButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/show_log_pressed.png")));
        ShowLogWinButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ShowLogWinButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ShowLogWinButton.setMaximumSize(new java.awt.Dimension(80, 60));
        ShowLogWinButton.setMinimumSize(new java.awt.Dimension(80, 60));
        ShowLogWinButton.setPreferredSize(new java.awt.Dimension(80, 60));
        ShowLogWinButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/show_log_pressed.png")));
        ShowLogWinButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/show_log_pressed.png")));
        ShowLogWinButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ShowLogWinButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ShowLogWinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuShowLogWin(evt);
            }
        });

        jPanel15.add(ShowLogWinButton);

        jPanel4.add(jPanel15);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 15));

        jPanel9.setBackground(new java.awt.Color(190, 190, 226));
        jLabel6.setBackground(new java.awt.Color(190, 190, 226));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iWork_enabled.png")));
        jPanel9.add(jLabel6);

        jPanel4.add(jPanel9);

        jPanel16.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        jPanel3.setBackground(new java.awt.Color(190, 190, 226));
        jPanel3.setPreferredSize(new java.awt.Dimension(575, 90));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 15));

        jPanel14.setBackground(new java.awt.Color(190, 190, 226));
        jPanel14.setMaximumSize(new java.awt.Dimension(380, 90));
        jPanel14.setMinimumSize(new java.awt.Dimension(380, 90));
        jPanel14.setPreferredSize(new java.awt.Dimension(380, 90));
        ConnectButton.setBackground(new java.awt.Color(190, 190, 226));
        ConnectButton.setFont(new java.awt.Font("Dialog", 0, 11));
        ConnectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/connect.png")));
        ConnectButton.setText("Connect");
        ConnectButton.setToolTipText("Connect");
        ConnectButton.setBorder(null);
        ConnectButton.setBorderPainted(false);
        ConnectButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/images/connect_pressed.png")));
        ConnectButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/connect_pressed.png")));
        ConnectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ConnectButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ConnectButton.setMaximumSize(new java.awt.Dimension(75, 60));
        ConnectButton.setMinimumSize(new java.awt.Dimension(75, 60));
        ConnectButton.setPreferredSize(new java.awt.Dimension(75, 60));
        ConnectButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/connect_pressed.png")));
        ConnectButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/connect_pressed.png")));
        ConnectButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ConnectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ConnectButton.setEnabled(false);
        ConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectButtonActionPerformed(evt);
            }
        });

        jPanel14.add(ConnectButton);

        NewButton.setBackground(new java.awt.Color(190, 190, 226));
        NewButton.setFont(new java.awt.Font("Dialog", 0, 11));
        NewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/new_profile.png")));
        NewButton.setText("New");
        NewButton.setToolTipText("Create new Connection Entry");
        NewButton.setBorder(null);
        NewButton.setBorderPainted(false);
        NewButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/images/connect_pressed.png")));
        NewButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/new_profile_pressed.png")));
        NewButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NewButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        NewButton.setMaximumSize(new java.awt.Dimension(65, 60));
        NewButton.setMinimumSize(new java.awt.Dimension(65, 60));
        NewButton.setPreferredSize(new java.awt.Dimension(65, 60));
        NewButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/new_profile_pressed.png")));
        NewButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/new_profile_pressed.png")));
        NewButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        NewButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        NewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewButtonActionPerformed(evt);
            }
        });

        jPanel14.add(NewButton);

        ImportButton.setBackground(new java.awt.Color(190, 190, 226));
        ImportButton.setFont(new java.awt.Font("Dialog", 0, 11));
        ImportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/import_profile.png")));
        ImportButton.setText("Import");
        ImportButton.setToolTipText("Import Connection file (.pcf)");
        ImportButton.setBorder(null);
        ImportButton.setBorderPainted(false);
        ImportButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/images/import_profile_pressed.png")));
        ImportButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/import_profile_pressed.png")));
        ImportButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ImportButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ImportButton.setMaximumSize(new java.awt.Dimension(65, 60));
        ImportButton.setMinimumSize(new java.awt.Dimension(65, 60));
        ImportButton.setPreferredSize(new java.awt.Dimension(65, 60));
        ImportButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/import_profile_pressed.png")));
        ImportButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/import_profile_pressed.png")));
        ImportButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ImportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportButtonActionPerformed(evt);
            }
        });

        jPanel14.add(ImportButton);

        ModifyButton.setBackground(new java.awt.Color(190, 190, 226));
        ModifyButton.setFont(new java.awt.Font("Dialog", 0, 11));
        ModifyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/modify_profile.png")));
        ModifyButton.setText("Modify");
        ModifyButton.setToolTipText("Modify Connection Entry");
        ModifyButton.setBorder(null);
        ModifyButton.setBorderPainted(false);
        ModifyButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/images/modify_profile_pressed.png")));
        ModifyButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/modify_profile_pressed.png")));
        ModifyButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ModifyButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ModifyButton.setMaximumSize(new java.awt.Dimension(65, 60));
        ModifyButton.setMinimumSize(new java.awt.Dimension(65, 60));
        ModifyButton.setPreferredSize(new java.awt.Dimension(65, 60));
        ModifyButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/modify_profile_pressed.png")));
        ModifyButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/modify_profile_pressed.png")));
        ModifyButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        ModifyButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ModifyButton.setEnabled(false);
        ModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifyButtonActionPerformed(evt);
            }
        });

        jPanel14.add(ModifyButton);

        DeleteButton.setBackground(new java.awt.Color(190, 190, 226));
        DeleteButton.setFont(new java.awt.Font("Dialog", 0, 11));
        DeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_profile.png")));
        DeleteButton.setText("Delete");
        DeleteButton.setToolTipText("Delete Connection Entry");
        DeleteButton.setBorder(null);
        DeleteButton.setBorderPainted(false);
        DeleteButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_profile_pressed.png")));
        DeleteButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_profile_pressed.png")));
        DeleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DeleteButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        DeleteButton.setMaximumSize(new java.awt.Dimension(65, 60));
        DeleteButton.setMinimumSize(new java.awt.Dimension(65, 60));
        DeleteButton.setPreferredSize(new java.awt.Dimension(65, 60));
        DeleteButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_profile_pressed.png")));
        DeleteButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_profile_pressed.png")));
        DeleteButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        DeleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        DeleteButton.setEnabled(false);
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        jPanel14.add(DeleteButton);

        jPanel3.add(jPanel14);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 15));

        jPanel5.setBackground(new java.awt.Color(190, 190, 226));
        jLabel3.setBackground(new java.awt.Color(190, 190, 226));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iWork_enabled.png")));
        jPanel5.add(jLabel3);

        jPanel3.add(jPanel5);

        jPanel16.add(jPanel3, java.awt.BorderLayout.NORTH);

        jTabbedPane1.setBackground(new java.awt.Color(190, 190, 226));
        jTabbedPane1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTabbedPane1.setFont(new java.awt.Font("Dialog", 0, 12));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(550, 350));
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                repaintTab(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(190, 190, 226));
        jPanel1.setPreferredSize(new java.awt.Dimension(350, 350));
        jScrollPane1.setBorder(null);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(20, 20));
        jScrollPane1.setAutoscrolls(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Connection Entry", "Description", "Host", "Transport"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jTable1.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 350));
        jTable1.setPreferredSize(new java.awt.Dimension(350, 350));
        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AccountKeyed(evt);
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                accountSelected(evt);
            }
        });

        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Connection Entries", null, jPanel1, "Connection Entries");

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(190, 190, 226));
        jPanel2.setPreferredSize(new java.awt.Dimension(350, 350));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showLogMenu(evt);
            }
        });

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jPanel10.setBackground(new java.awt.Color(190, 190, 226));
        jLabel7.setText("Log Message Level:");
        jPanel10.add(jLabel7);

        jComboBox1.setBackground(new java.awt.Color(190, 190, 226));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Error", "Warning", "Status", "Debug" }));
        jComboBox1.setDoubleBuffered(true);
        jComboBox1.setEnabled(false);
        jPanel10.add(jComboBox1);

        jPanel2.add(jPanel10, java.awt.BorderLayout.SOUTH);

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel11.setMaximumSize(new java.awt.Dimension(1200, 1200));
        LogWin.setEditable(false);
        LogWin.setLineWrap(true);
        LogWin.setWrapStyleWord(true);
        jScrollPane2.setViewportView(LogWin);

        jPanel11.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel11, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Log", null, jPanel2, "Log");

        jPanel16.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBackground(new java.awt.Color(190, 190, 226));
        jPanel6.setMinimumSize(new java.awt.Dimension(520, 50));
        jPanel6.setPreferredSize(new java.awt.Dimension(575, 50));
        StatusField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        StatusField.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        StatusField.setMaximumSize(new java.awt.Dimension(375, 25));
        StatusField.setMinimumSize(new java.awt.Dimension(375, 25));
        StatusField.setPreferredSize(new java.awt.Dimension(375, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        jPanel6.add(StatusField, gridBagConstraints);

        ProgressPanel.setLayout(new java.awt.GridLayout(1, 0, 1, 0));

        ProgressPanel.setBackground(new java.awt.Color(190, 190, 226));
        ProgressPanel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ProgressPanel.setMaximumSize(new java.awt.Dimension(150, 25));
        ProgressPanel.setMinimumSize(new java.awt.Dimension(150, 25));
        ProgressPanel.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 10);
        jPanel6.add(ProgressPanel, gridBagConstraints);

        jPanel16.add(jPanel6, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel16, java.awt.BorderLayout.CENTER);

        jMenuBar2.setBackground(new java.awt.Color(255, 255, 255));
        EntryMenu.setBackground(new java.awt.Color(255, 255, 255));
        EntryMenu.setMnemonic('C');
        EntryMenu.setText("Connection Entries");
        EntryMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        menuConnect.setFont(new java.awt.Font("Dialog", 0, 12));
        menuConnect.setMnemonic('C');
        menuConnect.setText("Connect");
        menuConnect.setToolTipText("Connect");
        menuConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConnect(evt);
            }
        });

        EntryMenu.add(menuConnect);

        menuDisconnect.setFont(new java.awt.Font("Dialog", 0, 12));
        menuDisconnect.setMnemonic('D');
        menuDisconnect.setText("Disconnect");
        menuDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDisconnect(evt);
            }
        });

        EntryMenu.add(menuDisconnect);

        EntryMenu.add(jSeparator1);

        menuModify.setFont(new java.awt.Font("Dialog", 0, 12));
        menuModify.setMnemonic('M');
        menuModify.setText("Modify...");
        menuModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuModifyAccount(evt);
            }
        });

        EntryMenu.add(menuModify);

        EntryMenu.add(jSeparator2);

        menuDelete.setFont(new java.awt.Font("Dialog", 0, 12));
        menuDelete.setMnemonic('E');
        menuDelete.setText("Delete");
        menuDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDeleteAccount(evt);
            }
        });

        EntryMenu.add(menuDelete);

        menuDuplicate.setFont(new java.awt.Font("Dialog", 0, 12));
        menuDuplicate.setMnemonic('U');
        menuDuplicate.setText("Duplicate");
        menuDuplicate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duplicateAccount(evt);
            }
        });

        EntryMenu.add(menuDuplicate);

        EntryMenu.add(jSeparator3);

        menuSetDefault.setFont(new java.awt.Font("Dialog", 0, 12));
        menuSetDefault.setMnemonic('S');
        menuSetDefault.setText("Set as Default Connection Entry");
        menuSetDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultAccount(evt);
            }
        });

        EntryMenu.add(menuSetDefault);

        EntryMenu.add(jSeparator4);

        menuNew.setFont(new java.awt.Font("Dialog", 0, 12));
        menuNew.setMnemonic('N');
        menuNew.setText("New ...");
        menuNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewAccount(evt);
            }
        });

        EntryMenu.add(menuNew);

        menuImport.setFont(new java.awt.Font("Dialog", 0, 12));
        menuImport.setMnemonic('I');
        menuImport.setText("Import...");
        menuImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuImportProfile(evt);
            }
        });

        EntryMenu.add(menuImport);

        EntryMenu.add(jSeparator5);

        menuConnectionWindow.setFont(new java.awt.Font("Dialog", 0, 12));
        menuConnectionWindow.setMnemonic('W');
        menuConnectionWindow.setText("Connection Window");
        menuConnectionWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showConnectionWindow(evt);
            }
        });

        EntryMenu.add(menuConnectionWindow);

        menuExit.setFont(new java.awt.Font("Dialog", 0, 12));
        menuExit.setMnemonic('X');
        menuExit.setText("Exit VPN Client");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitApp(evt);
            }
        });

        EntryMenu.add(menuExit);

        jMenuBar2.add(EntryMenu);

        StatusMenu.setBackground(new java.awt.Color(255, 255, 255));
        StatusMenu.setMnemonic('S');
        StatusMenu.setText("Status");
        StatusMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        menuStatistics.setFont(new java.awt.Font("Dialog", 0, 12));
        menuStatistics.setText("Statistics");
        menuStatistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showStatsWindow(evt);
            }
        });

        StatusMenu.add(menuStatistics);

        menuNotifications.setFont(new java.awt.Font("Dialog", 0, 12));
        menuNotifications.setText("Notifications");
        menuNotifications.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNotifications(evt);
            }
        });

        StatusMenu.add(menuNotifications);

        StatusMenu.add(jSeparator6);

        menuResetStats.setFont(new java.awt.Font("Dialog", 0, 12));
        menuResetStats.setText("Reset Stats");
        StatusMenu.add(menuResetStats);

        jMenuBar2.add(StatusMenu);

        LogMenu.setBackground(new java.awt.Color(255, 255, 255));
        LogMenu.setMnemonic('L');
        LogMenu.setText("Log");
        LogMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        menuEnable.setFont(new java.awt.Font("Dialog", 0, 12));
        menuEnable.setText("Enable");
        menuEnable.setEnabled(false);
        LogMenu.add(menuEnable);

        menuClear.setFont(new java.awt.Font("Dialog", 0, 12));
        menuClear.setMnemonic('C');
        menuClear.setText("Clear");
        menuClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClearLogWin(evt);
            }
        });

        LogMenu.add(menuClear);

        LogMenu.add(jSeparator7);

        menuLogSettings.setFont(new java.awt.Font("Dialog", 0, 12));
        menuLogSettings.setText("Log Settings");
        menuLogSettings.setEnabled(false);
        LogMenu.add(menuLogSettings);

        menuLogWin.setFont(new java.awt.Font("Dialog", 0, 12));
        menuLogWin.setMnemonic('W');
        menuLogWin.setText("Log Window");
        menuLogWin.setToolTipText("Log Window");
        menuLogWin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuShowLogWin(evt);
            }
        });

        LogMenu.add(menuLogWin);

        menuSearchLog.setFont(new java.awt.Font("Dialog", 0, 12));
        menuSearchLog.setText("Search Log");
        menuSearchLog.setEnabled(false);
        LogMenu.add(menuSearchLog);

        menuSaveLog.setFont(new java.awt.Font("Dialog", 0, 12));
        menuSaveLog.setText("Save");
        menuSaveLog.setEnabled(false);
        LogMenu.add(menuSaveLog);

        jMenuBar2.add(LogMenu);

        OptionsMenu.setBackground(new java.awt.Color(255, 255, 255));
        OptionsMenu.setMnemonic('O');
        OptionsMenu.setText("Options");
        OptionsMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        menuSimpleMode.setFont(new java.awt.Font("Dialog", 0, 12));
        menuSimpleMode.setMnemonic('s');
        menuSimpleMode.setText("Simple Mode");
        menuSimpleMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpleMode(evt);
            }
        });

        OptionsMenu.add(menuSimpleMode);

        menuPreferences.setFont(new java.awt.Font("Dialog", 0, 12));
        menuPreferences.setText("Preferences...");
        menuPreferences.setEnabled(false);
        OptionsMenu.add(menuPreferences);

        jMenuBar2.add(OptionsMenu);

        HelpMenu.setBackground(new java.awt.Color(255, 255, 255));
        HelpMenu.setMnemonic('H');
        HelpMenu.setText("Help");
        HelpMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        menuAbout.setFont(new java.awt.Font("Dialog", 0, 12));
        menuAbout.setMnemonic('A');
        menuAbout.setText("About ..");
        menuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbout(evt);
            }
        });

        HelpMenu.add(menuAbout);

        menuHelp.setFont(new java.awt.Font("Dialog", 0, 12));
        menuHelp.setMnemonic('H');
        menuHelp.setText("Help");
        menuHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHelp(evt);
            }
        });

        HelpMenu.add(menuHelp);

        jMenuBar2.add(HelpMenu);

        setJMenuBar(jMenuBar2);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-557)/2, (screenSize.height-391)/2, 557, 391);
    }//GEN-END:initComponents
    
    protected void showNotifications(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showNotifications
        // Add your handling code here:
        if ( NotificationWin instanceof NotifyWindow ){
            NotificationWin.clearWin();
            NotificationWin.setVisible(true);
            //NotificationWin.appendText("Generating Notification .." );
            NotificationWin.doNotification();
            
            
        } else {
            
            
            NotificationWin = new NotifyWindow(this);
            //NotificationWin.appendText("Generating Notification .." );
            NotificationWin.setVisible(true);
            NotificationWin.doNotification();
        }
        
    }//GEN-LAST:event_showNotifications
    
    protected void showHelp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showHelp
        // Add your handling code here:
        // create new Help Window or start mozilla?
        ////opt/local/bin/mozilla /opt/ITvpnclient/docs/SunStart.html
        String msg = "Click OK to display Help or enter a different URL";
        String title = "ITvpntool VPN3000 Help";
        String HelpUrl = null;
        
        String defaultUrl = "http://sun.com";
        String vpnUrl = "file:///opt/ITvpnclient/docs/SunStart.html";
        String COMPvpnUrl = "file:///opt/COMPvpn/docs/SunStart.html";
        String[] choices = { vpnUrl, COMPvpnUrl };
        for ( int i=0; i<choices.length; i++ ){
            
            try{
                URL url = new URL( choices[i] );
                url.openConnection();
                URLConnection uc = url.openConnection();
                uc.connect();
                HelpUrl = choices[i];
                break;
                
            } catch ( java.io.IOException iox ){
                //iox.printStackTrace();
            }
            
        }
        JFrame parent = new JFrame();
        parent.setIconImage(minimizeIcon.getImage());
        parent.setTitle(title);
        
        String url = null;
        Object x = null;
        //Object x = JOptionPane.showInputDialog( null, msg, "Help", JOptionPane.INFORMATION_MESSAGE, null, choices, defaultUrl );
        if ( HelpUrl == null ){
            msg = "No local help found. Enter a URL for Help if known";
            x = JOptionPane.showInputDialog( parent, msg, title, JOptionPane.INFORMATION_MESSAGE, null, null, defaultUrl );
            
        } else {
            x = JOptionPane.showInputDialog( parent, msg, title, JOptionPane.INFORMATION_MESSAGE, null, null, HelpUrl );
        }
        if ( x instanceof String ){
            url = (String)x ;
        }
        
        if ( url != null ) {
            new OpenUrl( this, url );
        }
        
    }//GEN-LAST:event_showHelp
    
    private void menuImportProfile(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuImportProfile
        // Add your handling code here:
        new ImportProfile(this);
    }//GEN-LAST:event_menuImportProfile
    
    protected void showStatsWindow(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showStatsWindow
        // Add your handling code here:
        if ( this.StatWindow instanceof StatsWindow ){
            
        } else {
            StatWindow = new StatsWindow(this);
        }
        
        StatWindow.getFocus();
        
    }//GEN-LAST:event_showStatsWindow
    
    protected void simpleMode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpleMode
        // Add your handling code here:
        if ( this.isVisible() == true ) {
            this.setVisible(false);
        }
        if ( this.SimpleWin.isVisible() == false ){
            StartupMode = 0;
            this.SimpleWin.AccountCombo.setSelectedIndex(this.getSelectedAccountRow());
            this.SimpleWin.setLocation(this.getLocation());
            this.SimpleWin.setVisible(true);
            this.SimpleWin.requestFocus();
            this.SimpleWin.repaint();
        }
        setStartupMode();
    }//GEN-LAST:event_simpleMode
    
    
    
    protected void menuShowLogWin(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuShowLogWin
        // Add your handling code here:
        
        if ( DetachedLogWin.isVisible() == false ){
            DetachedLogWin.setVisible(true);
        } else {
            DetachedLogWin.toFront();
        }
    }//GEN-LAST:event_menuShowLogWin
    
    protected void repaintWin(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_repaintWin
        // Add your handling code here:
        //this.validate();
        //this.repaint();
    }//GEN-LAST:event_repaintWin
    
    protected void AccountKeyed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AccountKeyed
        // Add your handling code here:
        if ( evt.getKeyCode() == evt.VK_TAB ){
            TableModel tm = jTable1.getModel();
            
            jTable1.setColumnSelectionAllowed(false);
            jTable1.setRowSelectionAllowed(true);
            //jTable1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            int row = jTable1.getSelectedRow();
            int col = jTable1.getSelectedColumn();
            //jTable1.setSelectionBackground(new Color( 190,190,226 ));
            
            System.out.println( "RowCount = " + tm.getRowCount() );
            System.out.println( "row = " + row );
            if ( row < 0 || col < 0 ) return;
            
            jTable1.setRowSelectionInterval(row,row);
            
            if ( tm.getValueAt(row,0) != null ){
                
                // call method to enable/disable buttons, etc.
                System.out.println( "Row selected: " + row );
                System.out.println("Account name: " + tm.getValueAt(row, 0));
                ConnectButton.setEnabled(true);
                DeleteButton.setEnabled(true);
                ModifyButton.setEnabled(true);
                NewButton.setEnabled(true);
                currentAcct = (Hashtable)Accounts.get(tm.getValueAt(row, 0));
                
            } else {
                ConnectButton.setEnabled(false);
            }
            
        }
        
    }//GEN-LAST:event_AccountKeyed
    
    protected void showConnectionWindow(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showConnectionWindow
        // Add your handling code here:
        this.jTabbedPane1.setSelectedIndex(0);
        this.jTable1.requestFocus();
        jPanel16.remove(jPanel4);
        this.jPanel4.setVisible(false);
        jPanel16.add(jPanel3, java.awt.BorderLayout.NORTH );
        this.jPanel3.setVisible(true);
        
    }//GEN-LAST:event_showConnectionWindow
    
    protected void repaintTab(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repaintTab
        
        //System.out.println( "repaint tab" );
        int index = jTabbedPane1.getSelectedIndex();
        
        if ( index == 0 ){
            //System.out.println( "index " + index );
            this.jPanel4.setVisible(false);
            this.jPanel3.setVisible(true);
            this.jPanel3.repaint();
            jPanel16.remove(jPanel4);
            jPanel16.add(jPanel3, java.awt.BorderLayout.NORTH );
            
        } else if ( index == 1 ){
            // System.out.println( "index " + index );
            
            this.jPanel4.setVisible(true);
            this.jPanel4.repaint();
            this.jPanel3.setVisible(false);
            jPanel16.remove(jPanel3);
            jPanel16.add(jPanel4, java.awt.BorderLayout.NORTH );
            
        }
        
        //jPanel16.repaint();
        
    }//GEN-LAST:event_repaintTab
    
    protected void menuAbout(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbout
        // Add your handling code here:
        new vpnAbout();
    }//GEN-LAST:event_menuAbout
    
    protected void menuNewAccount(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewAccount
        // Add your handling code here:
        NewButtonActionPerformed(evt);
    }//GEN-LAST:event_menuNewAccount
    
    protected void menuDeleteAccount(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDeleteAccount
        // Add your handling code here:
        DeleteButtonActionPerformed(evt);
    }//GEN-LAST:event_menuDeleteAccount
    
    protected void menuModifyAccount(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuModifyAccount
        // Add your handling code here:
        if ( jTable1.getSelectedRow() >= 0 ){
            ModifyButtonActionPerformed(evt);
        } else {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No account selected", "Modify Account", JOptionPane.OK_OPTION );
        }
    }//GEN-LAST:event_menuModifyAccount
    
    protected void duplicateAccount(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_duplicateAccount
        // Add your handling code here:
        // get selected account
        String acct = this.getAccountSelected();
        if ( acct == null || acct.length() == 0 ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No account selected", "Error", JOptionPane.OK_OPTION );
            return;
        }
        String newAcct = new String( "copy of " + acct );
        Hashtable acctInfo = this.getAccountInfo(acct);
        
        if ( Accounts.containsKey(newAcct) ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Account name " + newAcct + " already taken", "Error", JOptionPane.OK_OPTION );
            return;
        }
        // change filename
        
        String path = new String( getDefaultProfileDir() + "/" + newAcct + ".pcf" );
        acctInfo.put( ProfileFields.Filename, path );
        // create new entry in Accounts
        // add to table
        this.addAccount(newAcct, acctInfo );
        this.SimpleWin.loadAccounts();
        
        // save to file
        this.writeProfileFile(newAcct);
        
        
    }//GEN-LAST:event_duplicateAccount
    
    protected void setAccountButtons(){
        if ( jTable1.getSelectedRow() >= 0 ){
            this.ConnectButton.setEnabled(true);
            this.ModifyButton.setEnabled(true);
            this.NewButton.setEnabled(true);
            this.ImportButton.setEnabled(true);
            this.DeleteButton.setEnabled(true);
        }
        
    }
    protected void setDefaultAccount(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDefaultAccount
        // Add your handling code here:
        
        int row = jTable1.getSelectedRow();
        //int col = jTable1.getSelectedColumn();
        if ( row < 0  ) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No Account Selected", "Please Select an Account", JOptionPane.OK_OPTION );
            return;
        }
        DefaultTableModel tm = (DefaultTableModel)jTable1.getModel();
        
        // TEST
        tm.moveRow(row,row, 0);
        setDefaultRow(0);
        setDefaultAccountName((String)tm.getValueAt(0,0));
        tm.fireTableCellUpdated(0,0);
        tm.fireTableDataChanged();
        jTable1.setRowSelectionInterval(0,0);
        System.out.println( "Setting row 0 to default" );
        this.SimpleWin.loadAccounts();
        
        
        /*
        setDefaultRow(row);
        setDefaultAccountName((String)tm.getValueAt(row,0));
        System.out.println( "Default Account Name: " + getDefaultAccountName() );
        tm.fireTableCellUpdated(row, col);
        tm.fireTableDataChanged();
         jTable1.setRowSelectionInterval(row,row);
         *System.out.println( "Setting row " + row + " to default" );
         **/
        
        // set Buttons
        setAccountButtons();
    }//GEN-LAST:event_setDefaultAccount
    
    protected void menuClearLogWin(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClearLogWin
        // Add your handling code here:
        this.clearLogWin(evt);
        
    }//GEN-LAST:event_menuClearLogWin
    
    protected void clearLogWin(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearLogWin
        // Add your handling code here:
        this.LogWin.setText(null);
        this.LogWin.repaint();
        this.DetachedLogWin.clearLogWin(evt);
    }//GEN-LAST:event_clearLogWin
    
    protected void menuDisconnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDisconnect
        // Add your handling code here:
        // if ( Connected == false ) return;
        this.updateStatus("Disconnecting .." );
        // ConnectButtonActionPerformed(evt);
        new vpnDisconnectThread(this);
    }//GEN-LAST:event_menuDisconnect
    
    protected void menuConnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConnect
        // Add your handling code here:
        // any xtra reqm't, then call
        if ( Connected == true ) return;
        ConnectButtonActionPerformed( evt );
        
    }//GEN-LAST:event_menuConnect
    
    protected void exitApp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitApp
        
        // Add your handling code here:
        
        if ( this.Connected == true ){
            int response =
            JOptionPane.showConfirmDialog(new javax.swing.JFrame(),
            new String( "Do You want to disconnect? Click 'Yes' to disconnect and exit, Click 'No' to exit without disconnecting" ),
            "Disconnect?", JOptionPane.YES_NO_CANCEL_OPTION );
            if ( response == JOptionPane.YES_OPTION ){
                new vpnDisconnectThread(this);
            } else if ( response == JOptionPane.CANCEL_OPTION ){
                return;
            }
        }
        writeInitFile();
        System.exit(0);
        
        
    }//GEN-LAST:event_exitApp
    
    protected void EnableLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnableLogButtonActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_EnableLogButtonActionPerformed
    
    protected void showLogMenu(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showLogMenu
        // Add your handling code here:
        jPanel3.setVisible(false);
        
        
    }//GEN-LAST:event_showLogMenu
    
    protected int  confirmAccountDelete( String name ){
        
        return JOptionPane.showConfirmDialog(new javax.swing.JFrame(), new String( "Are you sure you wish to delete the account " + name + "?" ), "Confirm Delete", JOptionPane.YES_NO_OPTION );
        
        
    }
    
    protected void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
        // Add your handling code here:
        //get row, acctname
        // remove row from table, acct from hash
        DeleteButton.setEnabled(false);
        DefaultTableModel tm = (DefaultTableModel)jTable1.getModel();
        
        jTable1.setColumnSelectionAllowed(false);
        jTable1.setRowSelectionAllowed(true);
        //jTable1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        int row = jTable1.getSelectedRow();
        int col = jTable1.getSelectedColumn();
        //jTable1.setSelectionBackground(new Color( 190,190,226 ));
        //if ( row >= 0 & col >= 0 ){
        if ( row >= 0 ){
            //System.out.println( "Row selected: " + row );
            jTable1.setRowSelectionInterval(row,row);
            if ( tm.getValueAt(row,0) != null ){
                //ConnectButton.setEnabled(true);
                // DeleteButton.setEnabled(true);
                String acct = (String)tm.getValueAt(row, 0);
                
                if ( this.confirmAccountDelete(acct) != 0 ){
                    DeleteButton.setEnabled(true);
                    return;
                }
                
                System.out.println("Account name to delete: " + acct );
                tm.removeRow(row);
                
                tm.fireTableRowsDeleted(row,row);
                
                int rowCount = tm.getRowCount();
                if ( rowCount == row ){
                    row--;
                }
                if ( rowCount < 1 ){
                    ConnectButton.setEnabled(false);
                    DeleteButton.setEnabled(false);
                    ModifyButton.setEnabled(false);
                    
                } else {
                    ConnectButton.setEnabled(true);
                    DeleteButton.setEnabled(true);
                    ModifyButton.setEnabled(true);
                    jTable1.setRowSelectionInterval(row,row);
                }
                //tm.fireTableDataChanged();
                //tm.fireTableStructureChanged();
                
                this.deleteProfileFile(acct);
                this.Accounts.remove(acct);
                this.SimpleWin.loadAccounts();
                //DeleteButton.setEnabled(false);
                //ModifyButton.setEnabled(false);
                //ConnectButton.setEnabled(false);
                // set DefaultRow to -1 if row == DefaultRow
                if ( row == DefaultRow ){
                    setDefaultRow(-1);
                }
                
                
                
            } else {
                System.out.println( "No account to delete" );
                // ConnectButton.setEnabled(false);
            }
            
            int   numRows = tm.getRowCount();
            Dimension viewSize = this.jTable1.getPreferredScrollableViewportSize();
            viewSize.setSize( viewSize.getWidth(), ( numRows * jTable1.getRowHeight()) );
            this.jTable1.setPreferredScrollableViewportSize(viewSize);
            this.jTable1.setPreferredSize(viewSize);
        }
    }//GEN-LAST:event_DeleteButtonActionPerformed
    
    protected void ModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifyButtonActionPerformed
        // Add your handling code here:
        ModifyButton.setEnabled(false);
        new ModifyAccount(this, this.getAccountSelected(), this.getSelectedAccountRow() );
        ModifyButton.setEnabled(true);
        
    }//GEN-LAST:event_ModifyButtonActionPerformed
    
    protected void ImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportButtonActionPerformed
        // Add your handling code here:
        // JFileChooser FILES_ONLY
        new ImportProfile(this);
        this.SimpleWin.loadAccounts();
        // get File
        // read & parse
        // add to account list
        // write to directory if it wasn't located in directory
        
    }//GEN-LAST:event_ImportButtonActionPerformed
    
    protected void NewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewButtonActionPerformed
        // Add your handling code here:
        NewButton.setEnabled(false);
        new NewAccount(this);
        this.SimpleWin.loadAccounts();
        NewButton.setEnabled(true);
    }//GEN-LAST:event_NewButtonActionPerformed
    
    public void setConnectionStatus( boolean status ){
        this.Connected = status;
        //this.Connecting = false;
        if ( status == false ){
            this.updateStatus( "Disconnected" );
            this.ConnectionCancelled = true;
        }
    }
    public void updateStatus(String status){
        //JTextField StatusField = new JTextField();
        
        if (  status != null  ){
            this.StatusField.setText(status);
            this.SimpleWin.StatusField.setText(status);
        } else {
            if (  this.Connected == true   ) {
                this.StatusField.setText("Connected");
                this.SimpleWin.StatusField.setText("Connected");
                
                
            }else  {
                this.StatusField.setText("No Connection");
                this.SimpleWin.StatusField.setText("No Connection");
                
            }
        }
        
    }
    public void updateStatus(){
        
        if (  this.Connected == true   ) {
            this.StatusField.setText("Connected");
            this.SimpleWin.StatusField.setText( "Connected" );
            
        }else  {
            this.StatusField.setText("No Connection");
            this.SimpleWin.StatusField.setText( "No Connection" );
        }
        
    }
    
    public void scrollLogWin(){
        this.LogWin.setCaretPosition(LogWin.getText().length());
        
        /*
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(20);
         
        JViewport jv = jScrollPane2.getViewport();
         
        JScrollBar js = jScrollPane2.getVerticalScrollBar();
        int incr = js.getUnitIncrement();
         
        Point vp = jv.getViewPosition();
        int rowHeight = this.LogWin.getGraphics().getFontMetrics().getHeight();
        //int rowWidth = this.LogWin.getWidth();
        vp.y += rowHeight*2;
        vp.x = 0;
         
        jv.setViewPosition(vp );
         
         
        //LogWin.scrollRectToVisible(LogWin.getBounds());
         
        //this.LogWin.revalidate();
        //this.repaint();
         
        //this.LogWin.repaint();
        //this.jTabbedPane1.repaint();
        //this.jTabbedPane1.getParent().repaint();
        //this.repaint();
         **/
        
        this.invalidate();
    }
    
    public void updateLog( String txt ){
        this.LogWin.append(txt);
        this.DetachedLogWin.appendText(txt);
        this.scrollLogWin();
    }
    
    public void AuthDone(){
        this.waitAuth = false;
    }
    
    public void enableConnectButton(){
        ConnectButton.setIcon( new javax.swing.ImageIcon(getClass().getResource("/images/connect.png" )));
        ConnectButton.setText("Connect");
        ConnectButton.setEnabled(true);
        this.SimpleWin.ConnectButton.setText("Connect");
        this.SimpleWin.ConnectButton.setEnabled(true);
        validate();
    }
    
    public void enableDisconnectButton(){
        ConnectButton.setIcon( new javax.swing.ImageIcon(getClass().getResource("/images/disconnect.png" )));
        ConnectButton.setText("Disconnect");
        ConnectButton.setPreferredSize(ConnectButton.getMaximumSize());
        ConnectButton.setEnabled(true);
        this.SimpleWin.ConnectButton.setText("Disconnect");
        this.SimpleWin.ConnectButton.setEnabled(true);
        validate();
    }
    public void setConnectButton( ){
        
        if ( Connected == true || Connecting == true ) {
            enableDisconnectButton();
        } else if ( Connected == false && Connecting == false ){
            enableConnectButton();
        }
    }
    public void stopUpdates(){
        
        if ( this.StatWindow instanceof StatsWindow ){
            this.StatWindow.stopUpdates();
        }
        return;
        
    }
    public void startUpdates(){
        
        if ( this.StatWindow instanceof StatsWindow ){
            this.StatWindow.update();
        } else {
            StatWindow = new StatsWindow(this);
        }
        
    }
    
    protected void vpnTerminate(){
        this.enableConnectButton();
        new vpnDisconnectThread(this);
        this.updateStatus("Disconnected");
    }
    
    protected void vpnConnect( String acct ){
        //stopUpdates();
        updateStatus("Connecting .." );
        this.ConnectionCancelled = false;
        this.enableDisconnectButton();
        try {
            if ( ! ProgressBar.isAlive() )
                startProgressBar();
        } catch ( NullPointerException np ){
            
        }
        clientConnection = new vpnConnectThread(this, acct);
    }
    
    public  void startProgressBar(){
        System.out.println( "Start Progress Bar" );
        ProgressBar = new ProgressThread( getProgressPanel() );
        ProgressBar.start();
        SimpleWinProgressBar = new ProgressThread( SimpleWin.getProgressPanel() );
        SimpleWinProgressBar.start();
    }
    public  void stopProgressBar(){
        System.out.println( "Stop Progress Bar" );
        if ( ProgressBar instanceof ProgressThread )
            ProgressBar.stopThread();
        if ( SimpleWinProgressBar instanceof ProgressThread )
            SimpleWinProgressBar.stopThread();
    }
    
    
    public synchronized void clearProgressPanel(){
        /**
         * SwingUtilities.invokeLater(new Runnable(){
         * public void run() {
         * panel.removeAll();
         * }
         * });
         */
        if ( ProgressPanel instanceof JPanel ){
            ProgressPanel.removeAll();
            ProgressPanel.repaint();
        }
    }
    public void stopConnection(){
        //try {
        //   if ( this.clientConnection instanceof vpnConnectThread ){
        //this.clientConnection.stopConnection();
        //  }
        //}catch( Exception ex ){
        //   ex.printStackTrace();
        //}
    }
    public String getSelectedAcctAttribute( String key ) {
        return (String)this.currentAcct.get(key);
    }
    
    
    protected void ConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectButtonActionPerformed
        // Add your handling code here:
        // Connect
        // we have to lock all other activity
        // otherwise stuff could get really goofed up
        // easiest way is to make connect button model
        
        String acct = this.getAccountSelected();
        if ( Connected == false && Connecting == false ){
            if ( acct == null  ){
                JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No Account Selected", "Please Select an Account", JOptionPane.OK_OPTION );
                
                System.out.println( "Null Account Selected");
                ConnectButton.setEnabled(false);
                return;
            } else if( acct.length() < 1  ){
                JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No Account Selected", "Please Select an Account", JOptionPane.OK_OPTION );
                System.out.println( "No Account Selected");
                ConnectButton.setEnabled(false);
                return;
            }
            
            if ( ConnectButton.getText().equals("Connect") ){
                System.out.println( "Connecting using account " + acct );
                enableDisconnectButton();
                
                //ConnectButton.setText( "Disconnect" );
                // SimpleWin.ConnectButton.setText( "Disconnect" );
                this.Connecting = true;
                ConnectButton.setPreferredSize(ConnectButton.getMaximumSize());
                // change icon
                //ConnectButton.setIcon( new javax.swing.ImageIcon(getClass().getResource("/images/disconnect.png" )));
                //ConnectButton.setEnabled(true);
                currentAcct = (Hashtable)this.Accounts.get(acct);
                System.out.println( "Config file: " + currentAcct.get( "Filename" ));
                // connect
                //new AuthDialog(this);
                // try vpnConnect( new AuthDialog( this ));
                vpnConnect( acct );
                //vpnConnect( acct, this );
            } else {
                vpnTerminate();
                this.enableConnectButton();
                this.Connected = false;
                SimpleWin.ConnectButton.setText( "Connect" );
            }
        } else {
            
            if ( ConnectButton.getText() == "Disconnect" ){
                vpnTerminate();
                this.enableConnectButton();
                this.Connected = false;
                this.Connecting = false;
            }
            
        }
    }//GEN-LAST:event_ConnectButtonActionPerformed
    public int getSelectedAccountRow(){
        return jTable1.getSelectedRow();
    }
    
    public String getAccountSelected() {
        // Add your handling code here:
        
        TableModel tm = jTable1.getModel();
        
        int row = jTable1.getSelectedRow();
        //System.out.println( "Row selected: " + row );
        //System.out.println("Account name: " + tm.getValueAt(row, 0));
        
        //System.out.println ( "Button clicked at row " + tm.getRowCount() );
        return( row == -1 ? "" : (String)tm.getValueAt(row, 0) );
    }
    
    protected void accountSelected(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountSelected
        // Add your handling code here:
        
        TableModel tm = jTable1.getModel();
        jTable1.setColumnSelectionAllowed(false);
        jTable1.setRowSelectionAllowed(true);
        //jTable1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        int row = jTable1.getSelectedRow();
        int col = jTable1.getSelectedColumn();
        //jTable1.setSelectionBackground(new Color( 190,190,226 ));
        
        System.out.println( "RowCount = " + tm.getRowCount() );
        System.out.println( "row = " + row );
        if ( row < 0 || col < 0 ) return;
        
        jTable1.setRowSelectionInterval(row,row);
        
        if ( tm.getValueAt(row,0) != null ){
            
            // call method to enable/disable buttons, etc.
            System.out.println( "Row selected: " + row );
            System.out.println("Account name: " + tm.getValueAt(row, 0));
            ConnectButton.setEnabled(true);
            DeleteButton.setEnabled(true);
            ModifyButton.setEnabled(true);
            NewButton.setEnabled(true);
            currentAcct = (Hashtable)Accounts.get(tm.getValueAt(row, 0));
            
        } else {
            ConnectButton.setEnabled(false);
        }
        //tm.setValueAt(new String("row " + row ), row,0);
        //tm.setValueAt(new String("Column 1"), row,1);
        //tm.setValueAt(new String("Column 2"), row,2);
        
        //System.out.println ( "Button clicked at row " + tm.getRowCount() );
        //return tm.getValueAt(row, 0);
    }//GEN-LAST:event_accountSelected
    
    protected void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed
    
    protected void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed
    
    protected void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed
    
    /** Exit the Application */
    protected void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        if ( this.Connected == true ){
            if ( JOptionPane.showConfirmDialog(new javax.swing.JFrame(),
            new String( "Do You want to disconnect? Click 'Yes' to disconnect and exit, Click 'No' to exit without disconnecting" ),
            "Disconnect?", JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION ){
                
                new vpnDisconnectThread(this);
            }
        }
        writeInitFile();
        System.exit(0);
        
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new vpn3k();
    }
    
    protected Hashtable parseConfig( File fh ){
        
        Vector LockedFields = new Vector();
        Hashtable configInfo = new Hashtable();
        
        try {
            
            BufferedReader in = new BufferedReader(new FileReader(fh));
            String str;
            while ((str = in.readLine()) != null) {
                // split on =
                if ( str.indexOf("=") >= 0 ){
                    String[] key_value = str.split("=", 2 );
                    // don't include '!' character
                    if ( key_value[0].charAt(0) == '!' ){
                        // add to list of 'non-editable'
                        String key = key_value[0].substring(1);
                        LockedFields.add(key);
                        configInfo.put(key, key_value[1] );
                    } else {
                        configInfo.put(key_value[0], key_value[1] );
                    }
                }
            }
            configInfo.put( ProfileFields.Locked, LockedFields );
            configInfo.put( ProfileFields.Filename, fh.getAbsolutePath() );
            
            
            // add empty fields
            Class missingProfileFields = Class.forName("ProfileFields");
            Field[] allFields = missingProfileFields.getFields();
            
            Set configSet = configInfo.keySet();
            Object[] configFields = configSet.toArray();
            
            boolean dirtyFlag = false;
            
            if ( configFields.length < allFields.length ){
                
                boolean missingField = true;
                for ( int i=0; i<allFields.length; i++ ){
                    String missingKey = (String)allFields[i].getName();
                    for ( int j=0; j<configFields.length; j++ ){
                        String configField = (String)configFields[j];
                        if ( missingKey.compareTo(configField) == 0 ){
                            // ok
                            missingField = false;
                            break;
                        }
                    }
                    if ( missingField == true ){
                        // add to configInfo
                        if( missingKey.compareTo(ProfileFields.ProfileName) == 0 ){
                            configInfo.put( missingKey, fh.getName() );
                        } else {
                            configInfo.put( missingKey, "" );
                        }
                        dirtyFlag = true;
                    } else {
                        missingField = true;
                    }
                }
            }
            if ( dirtyFlag = true ){
                // writeProfileFile((String)configInfo.get(ProfileFields.ProfileName));
                dirtyFlag = false;
            }
            
        } catch ( Exception ex ){
            String msg = "Error opening File: " + ex.getLocalizedMessage() ;
            JOptionPane.showMessageDialog(null, msg );
            System.out.print( "File Error: ");
            ex.printStackTrace();
        }
        
        
        return configInfo;
    }
    
    protected String[] getProfileFieldList(){
        String[] StringArray = null;
        try{
            Class pf = Class.forName("ProfileFields");
            
            Field[] allFields = pf.getFields();
            StringArray = new String[ allFields.length ];
            
            
            for ( int i=0; i<allFields.length; i++ ){
                StringArray[i] = (String)allFields[i].getName();
            }
        } catch ( java.lang.ArrayIndexOutOfBoundsException ob ){
        } catch  (  java.lang.ClassNotFoundException nf ){
        }
        
        return StringArray;
    }
    
    protected Hashtable mergeConfigs( Hashtable oldConfig, Hashtable newConfig ){
        
        // first, add any old configs to new config which aren't in new config
        Set oldConfigSet = oldConfig.keySet();
        Set newConfigSet = newConfig.keySet();
        
        Object[] oldConfigKeys = oldConfigSet.toArray();
        Object[] newConfigKeys = newConfigSet.toArray();
        Vector lockedFields = new Vector();
        
        for ( int i=0; i<oldConfigKeys.length; i++ ){
            try{
                String oldKey = (String)oldConfigKeys[i];
                Object val = oldConfig.get(oldKey);
                String oldValue = "";
                
                if ( val instanceof String ){
                    oldValue = (String)val;
                } else if ( val instanceof Vector ){
                    lockedFields = (Vector)val;
                    newConfig.put( oldKey, lockedFields );
                    continue;
                }
                
                if ( newConfig.containsKey(oldKey) ){
                    String newValue = (String)newConfig.get(oldKey);
                    if ( newValue != null ){
                        if ( newValue.length() < 1 ){
                            // use old value
                            newConfig.put( oldKey, oldValue );
                        }
                    }
                } else {
                    newConfig.put( oldKey,  oldValue );
                }
            } catch ( java.lang.NullPointerException np ){
                System.out.println( np.getLocalizedMessage() );
            }
        }
        
        
        return newConfig;
        
        /*
         
     Class missingProfileFields = Class.forName("ProfileFields");
            Field[] allFields = missingProfileFields.getFields();
         
         
         
            //Set configSet = info.keySet();
            //Object[] configFields = configSet.toArray();
         
            boolean dirtyFlag = false;
         
            if ( configFields.length < allFields.length ){
         
                boolean missingField = true;
                for ( int i=0; i<allFields.length; i++ ){
                    String missingKey = (String)allFields[i].getName();
                    for ( int j=0; j<configFields.length; j++ ){
                        String configField = (String)configFields[j];
                        if ( missingKey.compareTo(configField) == 0 ){
                            // ok
                            missingField = false;
                            break;
                        }
                    }
                    if ( missingField == true ){
                        // add to configInfo
                        if( missingKey.compareTo(ProfileFields.ProfileName) == 0 ){
                            info.put( missingKey, (String)oldConfig.get(ProfileFields.ProfileName) );
                        } else {
                            info.put( missingKey, "" );
                        }
                        dirtyFlag = true;
                    } else {
                        missingField = true;
                    }
                }
            }
            if ( dirtyFlag = true ){
               // writeProfileFile((String)configInfo.get(ProfileFields.ProfileName));
                dirtyFlag = false;
            }
         **/
    }
    
    
    protected int editInitData(String heading, String key, String value ){
        // change InitFileData
        
        // get Hashtable containing key/values
        Hashtable ht;
        try {
            if ( InitFileData.containsKey(heading) ){
                ht = (Hashtable)InitFileData.get( heading );
                
                // replace old or add new key/value
                ht.put(key,value);
            } else {
                return -1;
            }
            
            // replace previous data under heading
            InitFileData.put(heading, ht);
            
        } catch ( java.lang.NullPointerException np ){
            
        }
        return 0;
    }
    protected int deleteProfileFile( String name ){
        // get filename from accts
        
        
        Hashtable acctInfo = new Hashtable();
        
        if ( this.Accounts.containsKey(name) ){
            acctInfo = (Hashtable)this.Accounts.get(name);
            if ( acctInfo.containsKey(ProfileFields.Filename) ){
                String filename = (String)acctInfo.get(ProfileFields.Filename);
                try {
                    File f = new File(filename);
                    if ( f.exists() ){
                        f.delete();
                    }
                }catch ( Exception ex ){
                    ex.printStackTrace();
                    return -1;
                }
            }
            
        }
        return 0;
    }
    
    protected int writeProfileFile( String name ){
        // should be in the Accounts database
        // get all
        // get hashtable of acct info
        if ( name.length() < 1 ){
            return -1;
        }
        String filePath = new String();
        final int MAX = 10;
        
        try {
            Hashtable ht = (Hashtable)Accounts.get( name );
            // backup existing, create new
            if ( ht.containsKey(ProfileFields.Filename) ){
                filePath = (String)ht.get( ProfileFields.Filename );
                File f = new File( filePath );
                
                // make MAX backups
                if ( f.exists() ){
                    String newName = filePath + ".bak";
                    
                    for ( int i=0; i<=MAX; i++ ){
                        File newFile = new File( newName );
                        if ( newFile.exists() ){
                            if ( i == MAX ){
                                f.renameTo( newFile );
                                break;
                            }
                            newName = new String( newName + i );
                            continue;
                        } else {
                            f.renameTo( newFile );
                        }
                    }
                }
            }
            // write file
            FileWriter fw = new FileWriter( filePath );
            
            // write heading
            fw.write( "[" + ProfileHeadings.MAIN + "]" + "\n");
            
            Enumeration en = ht.keys();
            Vector lockedFields = new Vector();
            
            if ( ht.containsKey(ProfileFields.Locked) ){
                Object obj = ht.get(ProfileFields.Locked);
                if ( obj instanceof Vector ){
                    lockedFields = (Vector)obj;
                }
                
            }
            while ( en.hasMoreElements() ){
                String key = (String)en.nextElement();
                if ( key.compareTo(ProfileFields.Locked) == 0 ){ continue; }
                
                Object v = ht.get(key);
                if ( v instanceof String ){
                    String value = (String)v;
                    String line = "";
                    if ( lockedFields.contains(key) ){
                        line = new String( "!" + key + "=" + value + "\n" );
                    } else {
                        line = new String( key + "=" + value + "\n" );
                    }
                    fw.write( line );
                }
            }
            fw.close();
        } catch ( IOException ex ){
            updateLog(  ex.getMessage() );
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error Writing Profile File", "Create Profile Failed", JOptionPane.OK_OPTION );
            return -1;
        }
        
        return 0;
    }
    
    
    // private int loadProfiles(){
    protected int writeInitFile(){
        Hashtable keyValues;
        
        try {
            FileWriter fw = new FileWriter( InitFile );
            if ( InitFileData instanceof Hashtable ){
                // parse it
                Enumeration headings = InitFileData.keys();
                while ( headings.hasMoreElements() ){
                    String heading = (String)headings.nextElement();
                    // write it to the file
                    String header = new String( "[" + heading + "]" + "\n" );
                    fw.write(header);
                    
                    // get key/value pairs
                    keyValues = (Hashtable)InitFileData.get(heading);
                    Enumeration keys = keyValues.keys();
                    while ( keys.hasMoreElements() ){
                        String key = (String)keys.nextElement();
                        String value = (String)keyValues.get(key);
                        String keyValue = new String( key + "=" + value + "\n" );
                        fw.write( keyValue );
                    }
                    
                }
            }
            fw.close();
        }catch ( IOException ie ){
            ie.printStackTrace();
        }
        return 0;
    }
    protected int createInitFile(){
        
        try {
            FileWriter init = new FileWriter( InitFile );
            
            // create headings
            init.write( "[" + InitHeadings.GUI + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_CERT + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_CLI + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_CM + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_CVPND + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_DIALER + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_FIREWALL + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_GUI + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_IKE + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_IPSEC + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_PPP + "]" + "\n" );
            init.write( "[" + InitHeadings.LOG_XAUTH + "]" + "\n" );
            init.write( "[" + InitHeadings.MAIN + "]" + "\n" );
            init.close();
        } catch ( java.io.IOException ioe ){
            return -1;
        }
        
        return 0;
    }
    protected int loadInitFile(){
        
        // open and parse vpnclient.ini
        
        File init = new File( InitFile );
        if ( init.exists() == false ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "VPN Init File" + InitFile + " not found", "Error", JOptionPane.OK_OPTION );
            // create empty init file and load
            createInitFile();
        }
        
        
        ParseConfigFile pcf = new ParseConfigFile(InitFile);
        if ( pcf.ParseConfig() != 0 ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Error parsing VPN Init File" + InitFile, "Error", JOptionPane.OK_OPTION );
            
            return -1;
        }
        // save the 'Sections' structure so we can write out the Ini file
        InitFileData = pcf.getList();
        
        Hashtable guiItems;
        Enumeration sections = pcf.getKeys();
        while ( sections.hasMoreElements() ){
            String section = (String)sections.nextElement();
            if ( section.compareTo(InitHeadings.GUI) == 0 ){
                guiItems = (Hashtable)pcf.getValue( section );
                // default entry
                
                if ( guiItems.containsKey("DefaultConnectionEntry" ) ){
                    String defaultEntry = (String)guiItems.get(InitFields.DEFAULT_CONNECTION_ENTRY);
                    if ( defaultEntry != null ){
                        this.setDefaultAccountName(defaultEntry);
                        // put default entry at to
                        DefaultTableModel tm = (DefaultTableModel)jTable1.getModel();
                        for ( int i=0; i<tm.getRowCount(); i++ ){
                            String name = (String)tm.getValueAt(i, 0);
                            if ( defaultEntry.compareTo(name) == 0 ){
                                tm.moveRow(i, i,  0 );
                                jTable1.setRowSelectionInterval(0,0);
                                tm.fireTableRowsUpdated(0,0);
                                this.setAccountButtons();
                                
                            }
                        }
                    }
                }
                
                if ( guiItems.containsKey("AdvancedView" ) ){
                    String StartupString = (String) guiItems.get( InitFields.ADVANCED_VIEW );
                    if ( StartupString.compareTo("0") == 0 ){
                        StartupMode = 0;
                    } else if ( StartupString.compareTo("1") == 0 ){
                        StartupMode = 1;
                    }
                }
                if ( guiItems.containsKey( InitFields.DNS_SERVER ) ){
                    DnsServer = (String)guiItems.get(InitFields.DNS_SERVER);
                }
            } else if ( section.compareTo("") == 0 ){
            }
        }
        
        // what are we looking for:
        // GUI -  DefaultConnectionEntry
        // main
        // logging info
        
        // view type
        
        return 0;
    }
    
    protected int loadConfigs() {
        // load .pcf files
        //   private String ConfigDir = "/etc/CiscoSystemsVPNClient";
        //private String ProfilesDir = new String ( ConfigDir + "/Profiles" );
        
        class pcfFilter implements FilenameFilter {
            
            public boolean accept( File dir, String name ){
                if ( name.endsWith( ".pcf" ) && dir.canRead() ) return true;
                return false;
            }
        }
        
        
        try {
            // create dir filehandle
            File ProfilesHandle = new File( ProfilesDir );
            if ( ProfilesHandle.exists() == false ){
                
                int decision =  JOptionPane.showConfirmDialog(
                new javax.swing.JFrame(),
                new String( "Profile Directory " + ProfilesDir +  " does not exist. Would you like to create it now?" ),
                "Profile Directory Not Found",
                JOptionPane.YES_NO_OPTION
                );
                
                if ( decision == 0 ){
                    if ( ProfilesHandle.mkdirs() != true ){
                        JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Unable to create Profile Directory", "Create Directory Failed", JOptionPane.OK_OPTION );
                        return -1;
                    }
                } else {
                    return -1;
                }
                
            }
            // get \.pcf files in profiles dir
            File[] configs = ProfilesHandle.listFiles( new pcfFilter());
            
            // Set Connection Entry to same name as .pcf file?
            Arrays.sort( configs );
            DefaultTableModel tm = new DefaultTableModel();
            tm.addColumn("Connection Entry");
            tm.addColumn("Description");
            tm.addColumn("Host");
            tm.addColumn("Transport");
            
            Object[] rowData = new Object[4];
            
            for ( int i=0; i<configs.length; i++ ){
                
                // open file, parse contents, put into hashtableZhuohua
                String acctName = new String();
                if ( configs[i].getName().indexOf('.') != -1 ){
                    // getName() from File instance, split "name.pcf"
                    // account name is "name"
                    String[] nameSplit = configs[i].getName().split("\\.", 2);
                    acctName = nameSplit[0];
                }
                
                // generate key/value pairs
                Hashtable config = this.parseConfig(configs[i]);
                
                //Accounts.put( config.get("Description"), config );
                Accounts.put( acctName, config );
                
                //Accounts.put( acctName, this.parseConfig(configs[i]) );
                // put configs into table
                //AccountModel = this.jTable1.getModel();
                
                //Hashtable ht = (Hashtable)Accounts.get(acctName);
                
                //rowData[0] = config.get("Description");
                rowData[0] = acctName;
                rowData[1] = config.get("Description");
                rowData[2] = config.get("Host");
                
                //rowData[3] = "TCP";
                String TransportValue = "N/A";
                rowData[3] = TransportValue;
                
                try {
                    TransportValue = (String)config.get("TunnelingMode");
                    int TunnelMode = new Integer(TransportValue).intValue();
                    rowData[3] = TunnelConstants[ TunnelMode ];
                } catch ( Exception ex ){
                    System.err.println( "No Tunneling Mode for account " + acctName );
                }
                
                //int TunnelMode = new Integer((String)config.get("TunnelingMode")).intValue();
                
                tm.addRow(rowData);
                
                this.jTable1.setModel(tm);
                //tm.fireTableStructureChanged();
                jTable1.addNotify();
                
            }
            
            
            int numRows = tm.getRowCount();
            
            //tm.fireTableStructureChanged();
            
            Dimension viewSize = this.jTable1.getPreferredScrollableViewportSize();
            //viewSize.setSize( viewSize.getWidth(),  ( viewSize.getHeight() + ( viewSize.getHeight() / numRows )));
            viewSize.setSize( viewSize.getWidth(), ( numRows * jTable1.getRowHeight()) );
            this.jTable1.setPreferredScrollableViewportSize(viewSize);
            this.jTable1.setPreferredSize(viewSize);
            
            
        } catch ( Exception ex ){
            ex.printStackTrace();
            return -1;
        }
        //
        return 0;
    }
    
    public void run() {
        
    }
    public String getUserID(){
        return this.UserID;
        
    }
    
    public String getTokenPass() {
        return this.TokenPass;
    }
    public void setUserID(String id){
        this.UserID = id;
        
    }
    
    public void setTokenPass( String pass ) {
        this.TokenPass = pass;
    }
    
    public boolean accountExists(java.lang.String name) {
        return Accounts.containsKey(name);
    }
    
    public String getDefaultProfileDir() {
        return this.ProfilesDir;
    }
    public int modifyAccount(String oldname, String newname, Hashtable newInfo, int row ) {
        
        // create row info
        // acctname, description, host, transport
        Vector info = new Vector();
        try {
            info.add(newname);
            info.add( newInfo.get("Description"));
            info.add( newInfo.get("Host"));
        } catch ( java.lang.NullPointerException np ){
            JOptionPane.showMessageDialog(new javax.swing.JFrame(), "A field is empty", "Error", JOptionPane.OK_OPTION );
            return -1;
        }
        //blech
        
        int TunnelMode = new Integer((String)newInfo.get("TunnelingMode")).intValue();
        info.add( this.TunnelConstants[TunnelMode]);
        
        
        // first transfer locked attributes to new Account
        Hashtable oldInfo = (Hashtable)this.Accounts.get(oldname);
        
        
        if ( oldInfo.containsKey(ProfileFields.Locked) ){
            Vector locked = (Vector)oldInfo.get(ProfileFields.Locked);
            newInfo.put( ProfileFields.Locked,  locked);
        }
        
        String path =  new String( this.getDefaultProfileDir() + "/" + newname + ".pcf" );
        newInfo.put( ProfileFields.Filename,  path );
        
        // then transfer any missing fields
        Hashtable mergedInfo = this.mergeConfigs(oldInfo, newInfo);
        
        deleteProfileFile(oldname);
        this.Accounts.remove(oldname);
        
        // FileName
        
        this.Accounts.put(newname, mergedInfo);
        
        DefaultTableModel tm = (DefaultTableModel)jTable1.getModel();
        tm.removeRow(row);
        tm.insertRow(row, info);
        //tm.addRow( info );
        
        tm.fireTableRowsInserted(tm.getRowCount(), tm.getRowCount() );
        jTable1.setRowSelectionInterval(row,row);
        //tm.fireTableDataChanged();
        //tm.fireTableStructureChanged();
        
        // write account
        writeProfileFile(newname);
        // load new simple configs
        this.SimpleWin.loadAccounts();
        return 0;
    }
    public int addAccount(java.lang.String name, java.util.Hashtable acctInfo) {
        
        // create row info
        // acctname, description, host, transport
        Vector info = new Vector();
        info.add(name);
        info.add( acctInfo.get("Description"));
        info.add( acctInfo.get("Host"));
        
        //blech
        String tunnelMode = (String)acctInfo.get("TunnelingMode");
        
        if ( tunnelMode.length() < 1 || tunnelMode == null ){
            // set a default
            info.add( this.TunnelConstants[0] );
        } else {
            int TunnelMode = new Integer( tunnelMode ).intValue();
            info.add( this.TunnelConstants[TunnelMode]);
        }
        
        
        
        this.Accounts.put(name, acctInfo);
        
        DefaultTableModel tm = (DefaultTableModel)jTable1.getModel();
        
        int numRows = tm.getRowCount();
        int rowLocation = ( jTable1.getSelectedRow() + 1 );
        tm.insertRow(rowLocation, info );
        
        //tm.addRow( info );
        
        tm.fireTableRowsInserted(rowLocation, rowLocation );
        tm.fireTableDataChanged();
        numRows = tm.getRowCount();
        
        //tm.fireTableStructureChanged();
        
        Dimension viewSize = this.jTable1.getPreferredScrollableViewportSize();
        //viewSize.setSize( viewSize.getWidth(),  ( viewSize.getHeight() + ( viewSize.getHeight() / numRows )));
        viewSize.setSize( viewSize.getWidth(), ( numRows * jTable1.getRowHeight()) );
        this.jTable1.setPreferredScrollableViewportSize(viewSize);
        this.jTable1.setPreferredSize(viewSize);
        //this.jTable1.setRowSelectionInterval((numRows-1),(numRows-1));
        this.jTable1.setRowSelectionInterval(rowLocation, rowLocation);
        
        JViewport jv = jScrollPane1.getViewport();
        
        JScrollBar js = jScrollPane1.getVerticalScrollBar();
        int incr = js.getUnitIncrement();
        
        Point vp = jv.getViewPosition();
        int rowHeight = this.LogWin.getGraphics().getFontMetrics().getHeight();
        vp.y += rowHeight;
        
        jv.setViewPosition(vp );
        this.repaint();
        
        
        
        setAccountButtons();
        return 0;
    }
    
    public java.util.Hashtable getAccountInfo(java.lang.String name) {
        
        if ( Accounts.containsKey(name) ){
            Object value = Accounts.get(name);
            if ( value instanceof Hashtable ){
                return (Hashtable)value;
            }
        }
        return null;
    }
    
    public int reloadConfigs() {
        class pcfFilter implements FilenameFilter {
            
            public boolean accept( File dir, String name ){
                if ( name.endsWith( ".pcf" ) && dir.canRead() ) return true;
                return false;
            }
        }
        
        
        try {
            // create dir filehandle
            File ProfilesHandle = new File( ProfilesDir );
            if ( ProfilesHandle.exists() == false ){
                
                int decision =  JOptionPane.showConfirmDialog(
                new javax.swing.JFrame(),
                new String( "Profile Directory " + ProfilesDir +  " does not exist. Would you like to create it now?" ),
                "Profile Directory Not Found",
                JOptionPane.YES_NO_OPTION
                );
                
                if ( decision == 0 ){
                    if ( ProfilesHandle.mkdirs() != true ){
                        JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Unable to create Profile Directory", "Create Directory Failed", JOptionPane.OK_OPTION );
                        return -1;
                    }
                } else {
                    return -1;
                }
                
            }
            // get \.pcf files in profiles dir
            File[] configs = ProfilesHandle.listFiles( new pcfFilter());
            // Set Connection Entry to same name as .pcf file?
            
            
            DefaultTableModel tm = new DefaultTableModel();
            tm.addColumn("Connection Entry");
            tm.addColumn("Description");
            tm.addColumn("Host");
            tm.addColumn("Transport");
            
            Object[] rowData = new Object[4];
            
            for ( int i=0; i<configs.length; i++ ){
                
                // open file, parse contents, put into hashtable
                String acctName = new String();
                if ( configs[i].getName().indexOf('.') != -1 ){
                    // getName() from File instance, split "name.pcf"
                    // account name is "name"
                    String[] nameSplit = configs[i].getName().split("\\.", 2);
                    acctName = nameSplit[0];
                }
                
                // generate key/value pairs
                Hashtable config = this.parseConfig(configs[i]);
                
                //Accounts.put( config.get("Description"), config );
                Accounts.put( acctName, config );
            }
        } catch ( Exception ex ){
            ex.printStackTrace();
            return -1;
        }
        //
        return 0;
    }
    
    public javax.swing.JPanel getProgressPanel() {
        return this.ProgressPanel;
    }
    public boolean isConnecting() {
        return this.Connecting;
    }
    public boolean isConnected() {
        return this.Connected;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton ClearLogButton;
    protected javax.swing.JButton ConnectButton;
    protected javax.swing.JButton DeleteButton;
    protected javax.swing.JButton EnableLogButton;
    protected javax.swing.JMenu EntryMenu;
    protected javax.swing.JMenu HelpMenu;
    protected javax.swing.JButton ImportButton;
    protected javax.swing.JMenu LogMenu;
    protected javax.swing.JButton LogSettingsButton;
    protected javax.swing.JTextArea LogWin;
    protected javax.swing.JButton ModifyButton;
    protected javax.swing.JButton NewButton;
    protected javax.swing.JMenu OptionsMenu;
    protected javax.swing.JPanel ProgressPanel;
    protected javax.swing.JButton ShowLogWinButton;
    protected javax.swing.JLabel StatusField;
    protected javax.swing.JMenu StatusMenu;
    protected javax.swing.ButtonGroup buttonGroup1;
    protected javax.swing.JButton jButton10;
    protected javax.swing.JButton jButton11;
    protected javax.swing.JButton jButton12;
    protected javax.swing.JCheckBox jCheckBox3;
    protected javax.swing.JComboBox jComboBox1;
    protected javax.swing.JFrame jFrame1;
    protected javax.swing.JLabel jLabel10;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JLabel jLabel5;
    protected javax.swing.JLabel jLabel6;
    protected javax.swing.JLabel jLabel7;
    protected javax.swing.JLabel jLabel8;
    protected javax.swing.JLabel jLabel9;
    protected javax.swing.JMenuBar jMenuBar2;
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JPanel jPanel10;
    protected javax.swing.JPanel jPanel11;
    protected javax.swing.JPanel jPanel12;
    protected javax.swing.JPanel jPanel13;
    protected javax.swing.JPanel jPanel14;
    protected javax.swing.JPanel jPanel15;
    protected javax.swing.JPanel jPanel16;
    protected javax.swing.JPanel jPanel17;
    protected javax.swing.JPanel jPanel18;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JPanel jPanel3;
    protected javax.swing.JPanel jPanel4;
    protected javax.swing.JPanel jPanel5;
    protected javax.swing.JPanel jPanel6;
    protected javax.swing.JPanel jPanel7;
    protected javax.swing.JPanel jPanel8;
    protected javax.swing.JPanel jPanel9;
    protected javax.swing.JRadioButton jRadioButton1;
    protected javax.swing.JRadioButton jRadioButton2;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JSeparator jSeparator1;
    protected javax.swing.JSeparator jSeparator2;
    protected javax.swing.JSeparator jSeparator3;
    protected javax.swing.JSeparator jSeparator4;
    protected javax.swing.JSeparator jSeparator5;
    protected javax.swing.JSeparator jSeparator6;
    protected javax.swing.JSeparator jSeparator7;
    protected javax.swing.JTabbedPane jTabbedPane1;
    protected javax.swing.JTable jTable1;
    protected javax.swing.JTextField jTextField1;
    protected javax.swing.JTextField jTextField2;
    protected javax.swing.JTextField jTextField3;
    protected javax.swing.JMenuItem menuAbout;
    protected javax.swing.JMenuItem menuClear;
    protected javax.swing.JMenuItem menuConnect;
    protected javax.swing.JMenuItem menuConnectionWindow;
    protected javax.swing.JMenuItem menuDelete;
    protected javax.swing.JMenuItem menuDisconnect;
    protected javax.swing.JMenuItem menuDuplicate;
    protected javax.swing.JMenuItem menuEnable;
    protected javax.swing.JMenuItem menuExit;
    protected javax.swing.JMenuItem menuHelp;
    protected javax.swing.JMenuItem menuImport;
    protected javax.swing.JMenuItem menuLogSettings;
    protected javax.swing.JMenuItem menuLogWin;
    protected javax.swing.JMenuItem menuModify;
    protected javax.swing.JMenuItem menuNew;
    protected javax.swing.JMenuItem menuNotifications;
    protected javax.swing.JMenuItem menuPreferences;
    protected javax.swing.JMenuItem menuResetStats;
    protected javax.swing.JMenuItem menuSaveLog;
    protected javax.swing.JMenuItem menuSearchLog;
    protected javax.swing.JMenuItem menuSetDefault;
    protected javax.swing.JMenuItem menuSimpleMode;
    protected javax.swing.JMenuItem menuStatistics;
    // End of variables declaration//GEN-END:variables
    protected static final String ConfigDir = "/etc/CiscoSystemsVPNClient";
    protected static final String ProfilesDir = new String( ConfigDir + "/Profiles" );
    protected static final String InitFile = new String( ConfigDir + "/vpnclient.ini" );
    protected static final String vpnclientBinary = "/usr/local/bin/vpnclient";
    protected Hashtable InitFileData;
    protected Hashtable Accounts = new Hashtable();
    protected Hashtable currentAcct= new Hashtable();
    //TableModel AccountModel;
    protected TableModel AccountModel;
    protected Hashtable ButtonHash;
    protected String UserID;
    protected String TokenPass;
    protected boolean waitAuth = true;
    protected boolean Connected = false;
    protected boolean Connecting = false;
    protected AuthDialog authDialog;
    protected String iconDir = "icons";
    protected static final Color SunBlue = new Color( 190,190,226);
    protected int DefaultRow = -1;
    protected String DefaultAccountName = null;
    public final String[] TunnelConstants = {"IPSec/UDP", "IPSec/TCP"};
    public final String pathDelim = System.getProperty("file.separator");
    protected vpnConnectThread clientConnection;
    protected LogWindow DetachedLogWin;
    protected NotifyWindow NotificationWin;
    protected String[] NotificationInfo = { "No Notifications" };
    protected vpn3kSimple SimpleWin;
    protected int StartupMode = 0; // 0=simple 1=advanced
    protected StatsWindow StatWindow;
    protected Hashtable Stats;
    protected boolean ConnectionCancelled = false;
    protected boolean initialStartup = true;
    public final String osName = System.getProperty("os.name");
    public final ImageIcon minimizeIcon =  new ImageIcon( getClass().getResource("images/minimize.png" ));
    public String DnsServer;
    private  ProgressThread ProgressBar;
    private ProgressThread SimpleWinProgressBar;
    
}

