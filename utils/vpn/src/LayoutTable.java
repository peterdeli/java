import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumnModel;

import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import javax.swing.event.ChangeEvent;

import java.awt.Dimension;
import java.awt.Insets;

import java.awt.Component;

/** JTable with some layout-configuration extensions.
    (C) 2003 Christian Kaufhold
*/
public class LayoutTable
    extends JTable
{
    public static final int SECONDARY = 0,
        FIXED_WIDTHS = 1,
        LAYOUT = 2;



    private int layoutMode = LAYOUT;

    private boolean headerInstalled = true;


    public LayoutTable(TableModel data)
    {
        super(data);
    }

    public LayoutTable(TableModel data, TableColumnModel columns)
    {
        super(data, columns);
    }



    protected void configureEnclosingScrollPane()
    {
        if (headerInstalled)
            super.configureEnclosingScrollPane();
    }
    
    protected void unconfigureEnclosingScrollPane()
    {
        if (headerInstalled)
             super.unconfigureEnclosingScrollPane();
    }

    
    /** Is the header automatically installed when the table
        is the main view of a JScrollPane?
        (It is useful to keep it around even if not used as
        a component so that the default resizing/dragging code 
        can be used)
        If true, you must not set other component as the row header
        view and not remove the header by hand etc.
        default: true
    */
    // property name?

    public final boolean isHeaderAutoInstalled()
    {
        return headerInstalled;
    }


    public void setHeaderAutoInstalled(boolean value)
    {
        if (value == headerInstalled)
            return;

        unconfigureEnclosingScrollPane();

        headerInstalled = value;
      
        configureEnclosingScrollPane();
    }






    /** current values:
        SECONDARY: table depends on another one to layout the columns.
        LAYOUT: columns are layed out according to the available space,
        the auto resize mode and their preferred widths?
        FIXED_WIDTHS: table takes widths from TableColumnModel verbatim
        (just like row heights).

        This property should only be changed when everything
        is in "a stable state".

        default: LAYOUT
    */
    public int layoutMode()
    {
        return layoutMode;
    }

    public void setLayoutMode(int value)
    {
        if (layoutMode == value)
            return;

        layoutMode = value;

        revalidate();
    }        





    public Dimension getMinimumSize()
    {
        if (layoutMode != FIXED_WIDTHS || isMinimumSizeSet())
            return super.getMinimumSize();

        return fixedPreferredSize();
    }


    public Dimension getMaximumSize()
    {
        if (layoutMode != FIXED_WIDTHS || isMaximumSizeSet())
            return super.getMaximumSize();

        return fixedPreferredSize();
    }

    public Dimension getPreferredSize()
    {
        if (layoutMode != FIXED_WIDTHS || isPreferredSizeSet())
            return super.getPreferredSize();

        return fixedPreferredSize();
    }


    private Dimension fixedPreferredSize()
    {
        Dimension result = new Dimension(super.getPreferredSize());

        Insets n = getInsets();

        result.width = getColumnModel().getTotalColumnWidth() + n.left + n.right;

        return result;
    }
    


    public boolean getScrollableTracksViewportWidth()
    {
        if (layoutMode == LAYOUT)
            return super.getScrollableTracksViewportWidth();
        
        // should this use fixedPreferredSize?
        return getParent().getWidth() > getPreferredSize().width; // do not underflow
    }




    public void columnMarginChanged(ChangeEvent e)
    {
        if (layoutMode == LAYOUT)
            super.columnMarginChanged(e);
        else
            resizeAndRepaint();
    }


    public void doLayout()
    {
        if (layoutMode == LAYOUT)
            super.doLayout();
    }


    public void sizeColumnsToFit(int column)
    {
        if (layoutMode == LAYOUT)
            super.sizeColumnsToFit(column);
    }






    public static void main(String[] args)
    {
        int columns = 13;

        LayoutTable m = new LayoutTable(new javax.swing.table.DefaultTableModel(20, columns));
        m.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        m.setHeaderAutoInstalled(false);

        javax.swing.JScrollPane p = new javax.swing.JScrollPane(m);

        LayoutTable h = new LayoutTable(new javax.swing.table.DefaultTableModel(1, columns), m.getColumnModel());

        h.setLayoutMode(LayoutTable.SECONDARY);

        // This is ugly. The problem that is that for determining scroll pane
        // layout, the column header's preferred scrollable viewport height is 
        // used (and not its preferred height), although the column header never
        // scrolls vertically.
        // This relies on the fact the the width is not used (but that
        // is quite safe).
        h.setPreferredScrollableViewportSize(h.getPreferredSize());
        
        p.setColumnHeaderView(h);

        javax.swing.JFrame f = new javax.swing.JFrame();

        f.getContentPane().add(p);
        f.getContentPane().add(m.getTableHeader(), "North");

        f.pack(); f.show();
    }
}

