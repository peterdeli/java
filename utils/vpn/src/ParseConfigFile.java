/*
 * parseInitFile.java
 *
 * Created on November 11, 2003, 10:05 AM
 */

/**
 *
 * @author  pdel
 */

import java.io.*;
import java.util.regex.*;
import java.util.*;

public class ParseConfigFile {
    
    /** Creates a new instance of parseInitFile */
    public ParseConfigFile() {
    }
    
    public ParseConfigFile(String filename){
        Fname = filename;
        FieldName = new String();
        Sections = new Hashtable();
        
        
    }
    
    public int ParseConfig(){
        // load vonclient.ini settings
        try {
            File initFile = new File( Fname );
            
            if ( initFile.exists() && initFile.canRead() ){
                BufferedReader in = new BufferedReader(new FileReader(initFile));
                
                String str;
                while ((str = in.readLine()) != null) {
                    // keys are [*], values follow as key=value
                    Pattern p = Pattern.compile("^\\[[A-Za-z\\.]+\\]");
                    Matcher m = p.matcher(str);
                    if ( m.find() ){
                        String field = m.group();
                        
                        p = Pattern.compile( "[\\[\\]]" );
                        m = p.matcher(field);
                        
                        String fieldName = m.replaceAll("");
                        
                        //System.out.println( "Found field: " + fieldName );
                        
                        // is this the first field?
                        if ( (FieldName.length() > 0) && (FieldName.equals(fieldName) == false) ){
                            // its not the first field
                            Sections.put(FieldName, Items);
                        }
                        FieldName = fieldName;
                        Items = new Hashtable();
                        
                        // create new entry in hashtable using fieldName
                        // add previous hashtable to section
                        // create new keyValue instance
                        
                    }else {
                       // System.out.println( str );
                        // split on '=', add to keyValue hashtable
                        if ( str.indexOf('=') > 0 ){
                            String[] KeyVal = str.split("=");
                            if ( KeyVal.length == 2 ){
                                
                                Items.put( KeyVal[0], KeyVal[1] );
                                
                            } else if ( KeyVal.length == 1 ){
                                Items.put( KeyVal[0], "");
                            }
                        }
                    }
                }
                // add entries if last time or only one FieldName
                
                if ( FieldName.length() > 0 ){
                 Sections.put(FieldName, Items);
                }
                
                
            } else {
                return -1;
            }
            
        } catch ( Exception ex ){
            ex.printStackTrace();
            return -1;
        }
        return 0;
    }
    
    public Hashtable getList(){
        // hashtable of sections each pointing to a hash of items
        return this.Sections;
    }
    
    public Object getValue( Object key ){
        // returns a hashtable of items
        // keys is [main], etc.
        return Sections.get(key);
    }
    public Enumeration getKeys(){
        // keys in the form of [main] [GUI], [LOG.xxx], etc.
        return this.Sections.keys();
    }
    
    
    
    public static void main(String[] args) {
        
        // example of how to get key/value pairs 
        
        
        // /etc/CiscoSystemsVPNClient/Profiles/ebay.pcf
        // /etc/CiscoSystemsVPNClient/vpnclient.ini
        
        ParseConfigFile pcf = new ParseConfigFile( args[0] );
        
        
        //ParseConfigFile pcf = new ParseConfigFile( "/etc/CiscoSystemsVPNClient/vpnclient.ini" );
        
        pcf.ParseConfig();
        System.out.println( "Done" );
        Enumeration pcfEnum = pcf.getKeys();
        while ( pcfEnum.hasMoreElements() ){
            Object key = (String)pcfEnum.nextElement();
            Object value = pcf.getValue(key);
            System.out.println( "Values for section " + (String)key + ": ");
            if ( value instanceof Hashtable ){
                Hashtable ht = (Hashtable)value;
                Enumeration en = ht.keys();
                while ( en.hasMoreElements() ){
                    String ItemKey = (String)en.nextElement();
                    System.out.println( "Key: " + ItemKey + " Value: " + ht.get(ItemKey) );
                }
            }
            
        }
    }
    
    private String Fname;
    private String FieldName;
    private Hashtable Items;
    private Hashtable Sections;
    
}
