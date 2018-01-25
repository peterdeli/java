
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;
import javax.swing.*;


public class ParseJava {


	public static void main( String[] args ){
            String command = "/home/pdel/vpn3k/vpn.expect";
            //String command = "/home/pdel/bin/vpn.sh";
            //String [] cmd_args = { command, "connect", "ebay" };
            String [] cmd_args = { "/usr/bin/expect", "-f", command };
            
            //String [] cmd_args = { "/bin/sh", "-c ", command  };
            //String [] cmd_args = { "/bin/sh", "-c ", command  };
            if ( new File( command ).exists() != true ) {
                JOptionPane.showMessageDialog(new javax.swing.JFrame(), "No such file " + command );
                System.exit(0);
            }
            boolean newLine = false;
            String lastWord = null;
            try {
                // Create the tokenizer to read from a file
                
                //String opts = " connect ebay";
                //String command_opts = new String ( command + opts );
                Process child = Runtime.getRuntime().exec(cmd_args);
                
                InputStream in = child.getInputStream();
                OutputStream out = child.getOutputStream();
                
                try {
                    Thread.sleep( 1000 );
                } catch ( Exception ex ) {
                    ex.printStackTrace();
                }
               /*
                while ( in.available() > 0 ){
                   System.out.print( (char) in.read() );
                    
                }
                **/
                
                StreamTokenizer st = new StreamTokenizer(in);
                
                // Prepare the tokenizer for Java-style tokenizing rules
                st.parseNumbers();
                st.wordChars('_', '_');
                st.eolIsSignificant(true);
                
                // If whitespace is not to be discarded, make this call
                st.ordinaryChars(0, ' ');
                
                // These calls caused comments to be discarded
                st.slashSlashComments(true);
                st.slashStarComments(true);
                
                // Parse the file
                //int token = st.nextToken();
                int token = 1;
                
                
                while (token != StreamTokenizer.TT_EOF) {
                    token = st.nextToken();
                    switch (token) {
                        case StreamTokenizer.TT_NUMBER:
                            // A number was found; the value is in nval
                            double num = st.nval;
                            System.out.print( num );
                            break;
                        case StreamTokenizer.TT_WORD:
                            // A word was found; the value is in sval
                            String word = st.sval;
                            lastWord = word;
                            System.out.print( word );
                            break;
                        case '"':
                            // A double-quoted string was found; sval contains the contents
                            String dquoteVal = st.sval;
                            break;
                        case '\'':
                            // A single-quoted string was found; sval contains the contents
                            String squoteVal = st.sval;
                            break;
                        case StreamTokenizer.TT_EOL:
                            // End of line character found
                            String eol = st.sval;
                            System.out.println();
                            // mark start of new line
                            newLine = true;
                            break;
                        case StreamTokenizer.TT_EOF:
                            // End of file has been reached
                            break;
                        default:
                            // A regular character was found; the value is the token itself
                            char ch = (char)st.ttype;
                            if ( ch == ':' ){
                                
                                System.out.print( ch );
                                // get space now
                                token = st.nextToken();
                                if ( token == ' ' ){
                                    // get all chars after last newline
                                    if ( lastWord.compareTo("Password" ) == 0 ) {
                                        // get input and send to process
                                        //System.out.print( "Enter password" );
                                        String response =  JOptionPane.showInputDialog( new javax.swing.JFrame(), "Enter Password: " );
                                        String password = response.concat("\n");
                                        //System.out.flush();
                                        
                                        out.write( password.getBytes() );
                                        out.flush();
                                        System.out.println();
                                        lastWord = "";
                                    }
                                }
                            } else {
                                System.out.print( ch );
                            }
                            break;
                    }
                }
                child.waitFor();
                
            } catch (IOException e) {
                e.printStackTrace();
            }catch ( InterruptedException ie ){
                ie.printStackTrace();
            }
        }
}
