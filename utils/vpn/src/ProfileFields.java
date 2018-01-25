/*
 * ProfileFields.java
 *
 * Created on November 9, 2003, 11:58 PM
 */

/**
 *
 * @author  pdel
 */
import java.util.*;

public interface ProfileFields {
    
    // fields for vpn3k .pcf file
    static final String Locked = "Locked";
    static final String Filename = "Filename";
    static final String ProfileName = "ProfileName";
    static final String Description = "Description";
    static final String Host = "Host";
    
    static final String AuthType = "AuthType";
    static final String GroupName = "GroupName";
    static final String GroupPwd = "GroupPwd";
    
    static final String Username = "Username";
    static final String SaveUserPassword = "SaveUserPassword";
    static final String EnableBackup = "EnableBackup";
    static final String BackupServer = "BackupServer";
    static final String EnableNat = "EnableNat";
    static final String TunnelingMode = "TunnelingMode";
    static final String TCPTunnelingPort = "TCPTunnelingPort";
    static final String EnableLocalLAN = "EnableLocalLAN";
    static final String DHGroup = "DHGroup";
    static final String ForceKeepAlives = "ForceKeepAlives";
    static final String enc_GroupPwd = "enc_GroupPwd";
    static final String UserPassword = "UserPassword";
    static final String enc_UserPassword = "enc_UserPassword";
    static final String EnableISPConnect = "EnableISPConnect";
    static final String ISPConnectType = "ISPConnectType";
    static final String ISPConnect = "ISPConnect";
    static final String ISPCommand = "ISPCommand";
    static final String NTDomain = "NTDomain";
    static final String EnableMSLogon = "EnableMSLogon";
    static final String MSLogonType = "MSLogonType";
    static final String CertStore = "CertStore";
    static final String CertName = "CertName";
    static final String CertPath = "CertPath";
    static final String CertSubjectName = "CertSubjectName";
    static final String CertSerialHash = "CertSerialHash";
    static final String SendCertChain = "SendCertChain";
    static final String PeerTimeout = "PeerTimeout";

}
