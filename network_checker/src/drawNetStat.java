/*
 * drawNetStat.java
 *
 * Created on January 6, 2004, 11:53 AM
 */

/**
 *
 * @author  Peter Delevoryas
 */

import javax.swing.*; // For JPanel, etc.
import java.awt.*;           // For Graphics, etc.
import java.awt.geom.*;      // For Ellipse2D, etc.

public class drawNetStat extends JPanel {
  
  private Rectangle2D.Double connectorA = new Rectangle2D.Double( 110, 60, 100, 20);
  private Rectangle2D.Double connectorB = new Rectangle2D.Double( 110, 60, 100, 20);
  private RoundRectangle2D.Double localHost = new RoundRectangle2D.Double( 10,10, 100,100, 20, 20 );
  private RoundRectangle2D.Double localGateway = new RoundRectangle2D.Double( 10,10, 100,100, 20, 20 );
  private RoundRectangle2D.Double vpnGateway = new RoundRectangle2D.Double( 10,10, 100,100, 20, 20 );
  
  

  public void paintComponent(Graphics g) {
      clear(g);
      Graphics2D g2d = (Graphics2D)g;
      //g2d.fill(circle);
      String family = "Serif";
      int style = Font.PLAIN;
      int size = 12;
      Font font = new Font(family, style, size);
      
      g2d.setFont(font);
      g2d.setColor(Color.green);
      g2d.draw(localHost);
      g2d.fill(localHost);
      
      g2d.setColor( Color.black );
      g2d.drawString("LocalHost", (float)(localHost.getX()+ 10.0 ), (float)(localHost.getCenterY()/2.0) );
      
      
      g2d.setColor(Color.red);
      
      double offsetW = ( localHost.getWidth() + localHost.getX() );
      double offsetH =  localHost.getHeight() ;
    
    connectorA.setFrame( offsetW, ( offsetH/2 ), 100, 20 );
    g2d.draw(connectorA);
    g2d.fill(connectorA);
    
    offsetW = ( connectorA.getWidth() + connectorA.getX() );
    offsetH =  connectorA.getHeight() ;
    localGateway.setFrame(offsetW, ( offsetH/2 ), 100, 100 );
    
    g2d.setColor(Color.green);
    g2d.draw(localGateway);
    g2d.fill(localGateway);
    
    g2d.setColor( Color.black );
    g2d.drawString("Gateway", (float)(localGateway.getX()+ 10.0 ), (float)(localGateway.getCenterY()/2.0) );
    
    offsetW = ( localGateway.getWidth() + localGateway.getX() );
    offsetH =  localGateway.getHeight() ;
    
    g2d.setColor(Color.red);
    connectorB.setFrame( offsetW, ( offsetH/2 ), 100, 20 );
    g2d.draw(connectorB);
    g2d.fill(connectorB);
    
    g2d.setColor(Color.green.darker());
    
    //vpnGateway
    offsetW = ( connectorB.getWidth() + connectorB.getX() );
    offsetH =  connectorB.getHeight() ;
    vpnGateway.setFrame(offsetW, ( offsetH/2 ), 100, 100 );
    g2d.draw(vpnGateway);
    g2d.fill(vpnGateway);
    
    g2d.setColor( Color.black );
    g2d.drawString("VPN Gateway", (float)(vpnGateway.getX()+ 10.0 ), (float)(vpnGateway.getCenterY()/2.0) );
    
    System.out.println( "Width is " + this.getWidth() );
  }

  // super.paintComponent clears offscreen pixmap,
  // since we're using double buffering by default.

  protected void clear(Graphics g) {
    super.paintComponent(g);
  }

  protected Dimension componentSize(){
    Rectangle rect =  getBounds();
    return rect.getSize();
  }
 

  public static void main(String[] args) {
      JFrame jf = new JFrame();
      drawNetStat netStat =  new drawNetStat();
      System.out.println( netStat.componentSize() );
      jf.getContentPane().add( netStat );
     
      //jf.getContentPane().add( new JPanel().add (netStat) );
      //jf.setSize();
    
      jf.setSize( 600, 250  );
      
        jf.show();
  }
}