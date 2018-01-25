import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ChangeEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Enumeration;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;



/** Just a default renderer. To work with TableHeader, the corresponding
    TableHeader must be read from the JTable client property (see code),
    since JTable.getTableHeader() cannot be used.
*/
class DefaultHeaderRenderer
    extends DefaultTableCellRenderer
{
    {
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    }

    public void updateUI()
    {
        super.updateUI();

        setHorizontalAlignment(CENTER);

        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    }


    public Component getTableCellRendererComponent
        (JTable table, Object value, boolean selected, boolean focused,
        int row, int column)
    {
        TableHeader header;

        if (table != null && (header = (TableHeader)table.getClientProperty(TableHeader.KEY)) != null)
        {
            setForeground(header.getForeground());
            setBackground(header.getBackground());

            if (focused)
                setBackground(table.getSelectionBackground());

            setFont(header.getFont());

            setComponentOrientation(header.getComponentOrientation());
            
            setEnabled(header.isEnabled());
        }
        else
        {
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));

            setFont(UIManager.getFont("TableHeader.font"));

            setComponentOrientation(ComponentOrientation.UNKNOWN);

            setEnabled(true);
        }

        setText(value != null ? value.toString() : "");
        
        return this;
    }
}



/** An alternative TableHeader that allows header cells to span multiple
    columns and boundaries which columns cannot be moved over.

    Note: JTable aggressively installs its JTableHeader in the column header
    view of JScrollPane, even if it is set to 'null'. To avoid confusion,
    override its (un-)configureEnclosingScrollPane() methods to do nothing, for
    example.

    Typically, a TableHeader is set up like this:

    TableHeader.XTableColumnModel columns = ...;

    // 'columns' contains some XTableColumns that have spans set
    // or some boundaries.

    JTable table = new FixedJTable(data, columns); // see above which fix

    table.setTableHeader(null);

    TableHeader header = new TableHeader(table);

    JScrollPane pane = new JScrollPane(table);

    pane.setColumnHeaderView(header);

    Modification of the TableColumnModel or disabling the header
    while it isDragging() or isResizing() is not allowed.

    The header works also with normal TableColumnModels (no boundaries)
    or normal TableColumns (no header spans).

    'opaque' is not supported. The header never paints its background. If
    'paintsResizeBackground' is set (true by default), it fills the resize
    arrow area with the associated JTable's background, if the JTable
    isOpaque() (that behaviour is still broken, but better.)

    Borders with changing Insets are not supported. These are evil anyway.

    BUGS / TO BE DONE:
    - No real interaction in a JScrollPane. Only once #4202002 is fixed,
    this can be done properly at all.
    - Resize area height strategy is weird.
    - Cursors.
    - how to serialize if the renderer isn't serializable?
    - Optimize paint code to paint only concerned columns
    - public Area conversion (columnAtPoint, headerRect or similar)
    - getToolTipText(MouseEvent)
    - Accessibility
    - Boundaries are not visible
*/

public class TableHeader
    extends JComponent
{
    /** TableColumn that also has a headerSpan property. 
        "Normal" TableColumns are assumed to have header span 1.

        If an XTableColumn has a headerSpan H > 1, then it must be followed
        (in the TableColumnModel) by H - 1 XTableColumns with headerSpan 0.
     */
    public static class XTableColumn
        extends TableColumn
    {
        private int headerSpan;
        
        
        public XTableColumn(int index)
        {
            this(index, 0);
        }


        public XTableColumn(int index, int span)
        {
            super(index);

            headerSpan = span;
        }
        
        /** number of columns that the header cell spans. */
        public final int headerSpan()
        {
            return headerSpan;
        }
        
        /** set 'headerSpan'. requires newHeaderSpan >= 0. During the
            manipulation of TableColumnModels, there may be times where
            a column spans more columns than there are at the right of it.
            This state is only allowed during such modifications. Once they
            are finished, all spans must not be too large. 
            XTableColumns that are hidden by the spans of their predecessors
            are ignored (in the TableHeader, of course not by the JTable).
            
            Due to the broken notification way of TableColumnModel/Table-
            Column there is no way to notify the header that it must 
            repaint().
            If firePropertyChange were not private, we could send the
            following fake event
            firePropertyChange("width", getWidth(), getWidth());
            which would handle that (only width and preferred width
            changes can reach the header).
            The moral is: don't change spans later, or if, repaint the
            header manually.
        */
        public void setHeaderSpan(int newHeaderSpan)
        {
            headerSpan = newHeaderSpan;   
        }
    }

    /** "cursor" = before the column with the same index. 
        Valid arguments are 0 ... columnCount (inclusive!).
        Boundaries are adjusted in the following way by adding/removal of columns:

        Addition of a column: Column is added after a boundary at the same place.
        Removal of a column: Boundaries before remains the same.
        Boundaries after shift by -1. That means if there was a boundary directly
        before and a boundary directy after the column, these collapse to one.

        Moving of the column  'column' to 'where':

        a) column < where (forwards)
           boundaries (column + 1, where) are shifted by -1
        b) column > where (backwards)
           boundaries (where + 1, column) are shifted by 1
        c) column == where
           nothing

        This way, as long as you don't move across boundaries (which
        is only possible programmatically), the boundaries will remain as they are.
     */
    public interface XTableColumnModel
        extends TableColumnModel
    {
        /** Add a boundary at 'cursor'. */
        void addBoundary(int cursor);
        /** Remove the boundary at 'cursor', if there is one. */
        void removeBoundary(int cursor);
        /** Remove all boundaries. */
        void clearBoundaries();
        /** Is the a boundary at 'cursor'? */
        boolean hasBoundary(int index);
    }
                


    public static class XDefaultTableColumnModel
        extends DefaultTableColumnModel
        implements XTableColumnModel
    {
        private ListSelectionModel boundaries;

        public XDefaultTableColumnModel()
        {
            boundaries = new DefaultListSelectionModel();
        }

        public void addBoundary(int cursor)
        {
            boundaries.addSelectionInterval(cursor, cursor);
        }

        public void removeBoundary(int cursor)
        {
            boundaries.addSelectionInterval(cursor, cursor);
        }

        public void clearBoundaries()
        {
            boundaries.clearSelection();
        }

        public void removeColumn(TableColumn c)
        {
            int index = TableColumnModels.indexOf(this, c);

            super.removeColumn(c);

            boundaries.removeIndexInterval(index, index);
        }

        public void moveColumn(int column, int where)
        {
            if (column != where)
            {
                boolean boundaryBefore = boundaries.isSelectedIndex(column);
                
                boundaries.removeIndexInterval(column, column);
                
                if (boundaryBefore)
                    boundaries.addSelectionInterval(column, column);
                
                if (column < where)
                {
                    boundaries.insertIndexInterval(where, 1, true);
                    boundaries.removeSelectionInterval(where, where);
                }
                else
                {
                    boundaries.insertIndexInterval(where, 1, false);
                    boundaries.removeSelectionInterval(where + 1, where + 1);
                }
            }

            super.moveColumn(column, where);
        }


        public boolean hasBoundary(int cursor)
        {
            return boundaries.isSelectedIndex(cursor);
        }
    }

    private class DragInfo
    {
        public int column;

        public int x, width;

        public int span;

        public int where;

        public int offset;

        public int lowerBoundary, upperBoundary;
    }

    

    private class Mouse
        implements MouseInputListener, PropertyChangeListener
    {
        private int lastX;


        public Mouse()
        {
            addPropertyChangeListener(this);
            
            if (isEnabled())
            {
                addMouseListener(this);
                addMouseMotionListener(this);
            }
        }

        public void dispose()
        {
            removeMouseListener(this);
            removeMouseMotionListener(this);

            removePropertyChangeListener(this);
        }

        
        public void propertyChange(PropertyChangeEvent e)
        {
            if (e.getPropertyName().equals("enabled"))
            {
                if (isEnabled())
                {
                    addMouseListener(this);
                    addMouseMotionListener(this);
                }
                else
                {
                    removeMouseListener(this);
                    removeMouseMotionListener(this);
                }
            }
        }

                    
        public void mouseEntered(MouseEvent e)
        {
        }

        public void mouseMoved(MouseEvent e)
        { 
        }

        public void mouseExited(MouseEvent e)
        {
        }


        public void mouseClicked(MouseEvent e)
        {
        }


        private int resizeColumnAt(int xx)
        { 
            int column = columns.getColumnIndexAtX(xx);
            
            if (column != -1)
            {
                int x = TableColumnModels.x(columns, column);
                
                int width = columns.getColumn(column).getWidth();
                
                // This is a heuristic. Rectangles of twice the height
                // than the width on each size of the column are resize regions.
                // This effectively makes the resize area square.
                int resizeWidth = getHeight() / 2;
                
                if (xx > x + resizeWidth && xx < x + width - resizeWidth)
                    return -1;
                
                // This implicitly returns -1 if it is the first column, which
                // is the right value, since columns are always resized on the right
                // so the first column cannot be resized on the left.
                return (xx > x + width / 2) ? column : column - 1;
            }
            
            return -1;
        }

        
        public void mousePressed(MouseEvent e)
        {
            if (!SwingUtilities.isLeftMouseButton(e))
                return;

            Point p = e.getPoint();
            Point q = getLocationOnScreen();
            lastX = p.x + q.x;

            if (p.y >= getHeight() - insets.bottom - resizeHeight())
            {
                resizedColumn = resizeColumnAt(p.x - insets.left);
                
                if (resizedColumn != -1  // && resizingEnabled (cannot happen)
                && columns.getColumn(resizedColumn).getResizable())
                {
                    repaint();
                }
                else
                    resizedColumn = -1;
            }
            else if (reorderingEnabled)
            {
                int column = cellAt(p);
        
                if (column != -1)
                {
                    int span = span(column);

                    boolean before = hasBoundary(column),
                        after = hasBoundary(column + span);

                    if (!((before && after)
                    || (column == 0 && after)
                    || (before && column + span == columns.getColumnCount())))

                    {
                        dragged = new DragInfo();

                        dragged.column = column;
                        dragged.where = column;

                        dragged.span = span;

                        dragged.x = TableColumnModels.x(columns, column);

                        dragged.offset = e.getX() - insets.left - dragged.x;

                        dragged.width = 0;

                        for (int i = 0; i < span; i++)
                            dragged.width += columns.getColumn(column + i).getWidth();
                        if (before)
                            dragged.lowerBoundary = column;
                        else
                        {
                            int lower = -1;

                            for (int i = column - 1; i >= 0; --i)
                                if (hasBoundary(i))
                                {
                                    lower = i;
                                    break;
                                }
                            
                            dragged.lowerBoundary = lower;
                        }


                        if (after)
                            dragged.upperBoundary = column;
                        else
                        {
                            int upper = columns.getColumnCount();

                            for (int count = columns.getColumnCount(), i = column + 1; i < count; i++)
                                if (hasBoundary(i))
                                {
                                    upper = i;
                                    break;
                                }
                            
                            dragged.upperBoundary = upper - span;
                        }

                        repaint();
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e)
        { 
            if (!SwingUtilities.isLeftMouseButton(e))
                return;


            if (resizedColumn != -1)
            {
                repaint();

                resizedColumn = -1;
            }
            else if (dragged != null)
            {
                if (dragged.where > dragged.column)
                {
                    for (int i = dragged.span - 1; i >= 0; --i)
                        columns.moveColumn(dragged.column + i, dragged.where + i);
                }
                else if (dragged.where < dragged.column)
                {
                    for (int i = 0; i < dragged.span; i++)
                        columns.moveColumn(dragged.column + i, dragged.where + i);
                }

                dragged = null;

                repaint();
            }
        }
       

        public void mouseDragged(MouseEvent e)
        {
            int newX = e.getX() + getLocationOnScreen().x;
            int dx = newX - lastX;

            if (dx == 0)
                return;

            if (resizedColumn != -1)
            {
                TableColumn c = columns.getColumn(resizedColumn);
                {
                    int width = c.getWidth();

                    // set width fill fire column margin changed
                    // which already causes the header to repaint
                    // JTable will unfortunately revalidate()
                    // probably unnecessarily
                    c.setWidth(width + dx);

                    table().sizeColumnsToFit(resizedColumn);
                    
                    int newWidth = c.getWidth();
                    
                    lastX += newWidth - width;
                }
            }
            else if (dragged != null)
            {
                int x = e.getX() - insets.left - dragged.offset;

                int groupStart, groupSpan = 0, groupWidth = 0;
                
                if (x < dragged.x)
                {
                    if (x < 0)
                        groupStart = 0;
                    else
                        groupStart = columnAt(x);

                    
                    groupSpan = span(groupStart);
                    
                    for (int i = 0; i < groupSpan; i++)
                        
                        groupWidth += columns.getColumn(groupStart + i).getWidth();
                    
                    int groupX = TableColumnModels.x(columns, groupStart);
                    
                    if (x <= groupX + groupWidth / 2)
                        dragged.where = groupStart;
                    else
                        dragged.where = groupStart + groupSpan;

                    dragged.where = Math.max(dragged.where, dragged.lowerBoundary);    
                }
                else
                {
                    if (x + dragged.width >= columns.getTotalColumnWidth())
                        dragged.where = columns.getColumnCount() - dragged.span;
                    else
                    {
                        groupStart = columnAt(x + dragged.width);
                    
                        groupSpan = span(groupStart);
                        
                        for (int i = 0; i < groupSpan; i++)
                            
                            groupWidth += columns.getColumn(groupStart + i).getWidth();
                        
                        int groupX = TableColumnModels.x(columns, groupStart);
                        
                        if (x + dragged.width > groupX + groupWidth / 2)
                            dragged.where = groupStart + groupSpan - dragged.span;
                        else
                            dragged.where = groupStart - dragged.span;
                    }

                    dragged.where = Math.min(dragged.where, dragged.upperBoundary);
                }

                repaint();
            }
        }
    }


    private boolean hasBoundary(int cursor)
    {
        return columns instanceof XTableColumnModel
            && ((XTableColumnModel)columns).hasBoundary(cursor);
    }

    private int span(int column)
    {
        return span(columns.getColumn(column));
    }

    private int span(TableColumn c)
    {
        return c instanceof XTableColumn
            ? ((XTableColumn)c).headerSpan()
            : 1;
    }        


    private static Insets ZERO_INSETS = new Insets(0, 0, 0, 0);

    
    public final void setBorder(Border b)
    {
        super.setBorder(b);

        if (b == null)
            insets = ZERO_INSETS;
        else
            insets = b.getBorderInsets(this);
    }

    public final Insets getInsets()
    {
        return insets = super.getInsets();
    }

    public final Insets getInsets(Insets result)
    {
        return insets = super.getInsets(result);
    }




    private Insets insets = ZERO_INSETS;



    private static TableCellRenderer staticDefaultRenderer
        = new DefaultHeaderRenderer();

    /** Key of the client property of JTable for the table header, so that
        the renderer can access it. See demo renderer above.
    */
    public static Object KEY = TableHeader.class;

    private JTable table;

    private transient TableColumnModel columns;

    private TableCellRenderer defaultRenderer
        = staticDefaultRenderer;

    
    private boolean resizingEnabled = true,
        reorderingEnabled = true;

    private transient int resizedColumn = -1;

    private boolean paintsResizeBackground = true;

    private Color dragOutline = Color.red;

    private transient DragInfo dragged;


    private transient Listener listener;


    public TableHeader(JTable table)
    {
        this.table = table;

        MouseListener mouse = new Mouse();

        table.putClientProperty(KEY, this);

        this.columns = table.getColumnModel();

        this.listener = createListener();

        table.addPropertyChangeListener(listener);

        columns.addColumnModelListener(listener);

        add(new CellRendererPane());

        updateUI();
    }


    public void updateUI()
    {
        LookAndFeel.installColorsAndFont
            (this, "TableHeader.background", "TableHeader.foreground",
            "TableHeader.font");

        LookAndFeel.installBorder(this, "TableHeader.border");

        if (defaultRenderer instanceof JComponent)
            ((JComponent)defaultRenderer).updateUI();

        revalidate(); repaint();
    }

    public boolean isDragging()
    {
        return dragged != null;
    }

    public boolean isResizing()
    {
        return resizedColumn != -1;
    }


    public final JTable table()
    {
        return table;
    }

    public void setTable(JTable t)
    {
        JTable oldTable = table;
        TableColumnModel oldColumns = columns;

        table.putClientProperty(KEY, null);

        table.removePropertyChangeListener(listener);
        
        columns.removeColumnModelListener(listener);

        table = t;

        table.putClientProperty(KEY, this);

        columns = t.getColumnModel();

        table.addPropertyChangeListener(listener);

        columns.addColumnModelListener(listener);

        revalidate(); repaint();

        firePropertyChange("table", oldTable, table);
        firePropertyChange("columns", oldColumns, columns);
    }

    public final TableColumnModel columns()
    {
        return columns;
    }

    /** For serialization, the TableCellRenderer is needed to be serializable.
     */
    public void setDefaultRenderer(TableCellRenderer r)
    {
        TableCellRenderer oldRenderer = defaultRenderer;

        defaultRenderer = r;
        
        revalidate(); repaint();

        firePropertyChange("defaultRenderer", oldRenderer, defaultRenderer);
    }

    public final TableCellRenderer defaultRenderer()
    {
        return defaultRenderer;
    }


    public void setResizingEnabled(boolean what)
    {
        if (what == resizingEnabled)
            return;

        resizingEnabled = what;

        revalidate(); repaint();
        
        firePropertyChange("resizingEnabled", !what, what);
    }

    public final boolean resizingEnabled()
    {
        return resizingEnabled;
    }


    public void setReorderingEnabled(boolean what)
    {
        if (what == reorderingEnabled)
            return;

        reorderingEnabled = what;

        revalidate(); repaint();
        
        firePropertyChange("reorderingEnabled", !what, what);
    }

    public final boolean reorderingEnabled()
    {
        return reorderingEnabled;
    }

    public void setDragOutline(Color c)
    {
        dragOutline = c;
    }

    public Color dragOutline()
    {
        return dragOutline;
    }


    private TableCellRenderer renderer(TableColumn c)
    {
        TableCellRenderer result = c.getHeaderRenderer();

        if (result != null)
            return result;

        return defaultRenderer;
    }


    private Component component(TableCellRenderer r, TableColumn c, int column)
    {
        return r.getTableCellRendererComponent
            (table, c.getHeaderValue(), false, dragged != null && dragged.column == column, -1, column);
    }


    private Dimension size(long innerWidth)
    {
        return new Dimension((int)Math.min(innerWidth + insets.left + insets.bottom, Integer.MAX_VALUE), cellHeight() + resizeHeight() + insets.top + insets.bottom);
    }

    private int resizeHeight()
    {
        return resizingEnabled ? table.getRowHeight() / 3 : 0;
    }

    
    /** Alas, this cannot be cached. */
    private int cellHeight()
    {
        int result = 0;

        int count = columns.getColumnCount();

        for (int j = 0; j < count; )
        {
            TableColumn c = columns.getColumn(j);

            int span = span(c);

            Component d = component(renderer(c), c, j);
            
            result = Math.max(result, d.getPreferredSize().height);
    
            j += span;
        }

        return result;
    }
 


    public Dimension getMinimumSize()
    {
        if (isMinimumSizeSet())
            return super.getMinimumSize();

        return size(TableColumnModels.minWidth(columns));
    }

    public Dimension getPreferredSize()
    {
        if (isPreferredSizeSet())
            return super.getPreferredSize();

        return size(TableColumnModels.preferredWidth(columns));
    }

    public Dimension getMaximumSize()
    {
        if (isMaximumSizeSet())
            return super.getMaximumSize();

        return size(TableColumnModels.maxWidth(columns));
    }



    private int cellAt(Point p)
    {
        if (p.y >= insets.top && p.y < getHeight() - resizeHeight() - insets.top - insets.bottom)
            return columnAt(p.x);

        return -1;
    }

    private int resizeAreaAt(Point p)
    {
        int height = getHeight();

        if (p.y >= height - resizeHeight() - insets.top - insets.bottom && p.y < height)
            return columnAt(p.x);
        
        return -1;
    }

    private int columnAt(int x)
    {
        int index = columns.getColumnIndexAtX(x - insets.left);

        if (index == -1)
            return -1;

        for (int k = index; k >= 0; --k)
        {
            if (span(columns.getColumn(k)) > 0)
                return k;
        }

        return index;
    }


    public void setPaintsResizeBackground(boolean value)
    {
        if (paintsResizeBackground == value)
            return;

        paintsResizeBackground = value;

        repaint();
    }

    public boolean paintsResizeBackground()
    {
        return paintsResizeBackground;
    }


    public void setOpaque(boolean value)
    {
    }



    private Rectangle bounds(int column)
    {
        Rectangle result = new Rectangle();

        result.x = insets.left;
        result.y = insets.top;
        result.height = getHeight() - resizeHeight() - insets.top - insets.bottom;


        int index = 0, size = columns.getColumnCount();

        while (index < size)
        {
            TableColumn c = columns.getColumn(index);

            int span = span(c);

            if (column >= index && column < index + span)
            {
                result.width = c.getWidth();

                for (int k = 1; k < span; k++)
                    result.width += columns.getColumn(index + k).getWidth();
                
                return result;
            }
            
            result.x += c.getWidth();
            
            for (int k = 1; k < span; k++)
                result.x += columns.getColumn(index + k).getWidth();
            
            index += span;
        }

        return result;
    }



    public void paintComponent(Graphics g)
    {
        Rectangle clip = g.getClipBounds();

        CellRendererPane pane = (CellRendererPane)getComponent(0);

        Rectangle r = new Rectangle();

        int resizeHeight = resizeHeight();


        if (resizingEnabled)
        {
            int x = insets.left,
                y = insets.top,
                width = Math.min(getWidth() - insets.left - insets.right, table.getWidth()),
                height = getHeight() - insets.top - insets.bottom;

            
            if (paintsResizeBackground && table.isOpaque()) // should be table.paintsBackground())
            {
                g.setColor(table.getBackground());
                
                g.fillRect(0, height - resizeHeight, width, resizeHeight);
            }
            
            g.setColor(table.getGridColor());
            
            g.drawLine(0, height - 1, width - 1, height - 1);
            
            for (int count = columns.getColumnCount(), i = 0; i < count; i++)
            {
                TableColumn c = columns.getColumn(i);
                
                x += c.getWidth();
                
                if (c.getResizable())
                {
                    int[] xp = { x - resizeHeight, x + resizeHeight, x };
                    int[] yp = { height - resizeHeight, height - resizeHeight, height - 1 };
                    
                    g.fillPolygon(xp, yp, 3);
                }
                else
                {
                    g.drawLine(x - 1, height - resizeHeight, x - 1, height - 1);
                }
            }
        }

        r.x = insets.left;
        r.y = insets.top;
        r.height = getHeight() - resizeHeight - insets.top - insets.bottom;
        
        if (r.height <= 0)
            return;

        int count = columns.getColumnCount();

        for (int j = 0; j < count; )
        {
            TableColumn c = columns.getColumn(j);

            r.width = c.getWidth();

            int span = span(c);

            if (j + span > count)
            {
                System.err.println("column: "+j+" span: "+span+" > "+count);
                System.err.println("This state of TableColumnModel is forbidden!");
                span = count - j;
            }
            
            for (int k = 1; k < span; k++)
                r.width += columns.getColumn(j + k).getWidth();

            Component d = component(renderer(c), c, j);
                    
            pane.paintComponent(g, d, this, 
                r.x, r.y, r.width, r.height, true);

            r.x += r.width;
    
            j += span;
        }


        
        
        if (dragged != null)
        {
            g.setColor(dragOutline);

            Rectangle bounds;

            if (dragged.where <= dragged.column)
                bounds = bounds(dragged.where);
            else
            {
                bounds = bounds(dragged.where + dragged.span);

                bounds.x -= dragged.width;
            }

            bounds.width = dragged.width;
           
            g.drawRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
        }


        pane.removeAll();
    }




    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        listener = createListener();

        table.addPropertyChangeListener(listener);

        columns = table.getColumnModel();

        columns.addColumnModelListener(listener);
    }
               

    protected Object clone()
        throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }


    private Listener createListener()
    {
        return new Listener();
    }


    private class Listener
        implements TableColumnModelListener, PropertyChangeListener
    {
        public void propertyChange(PropertyChangeEvent e)
        {
            String name = e.getPropertyName();

            if (name.equals("columnModel"))
            {
                TableColumnModel oldColumns = columns;

                columns.removeColumnModelListener(this);

                columns = table.getColumnModel();

                columns.addColumnModelListener(this);

                revalidate(); repaint();

                firePropertyChange("columns", oldColumns, columns);
            }
            else if (name.equals("gridColor") && resizingEnabled)
            {
                repaint();
            }
            else if (name.equals("rowHeight") && resizingEnabled)
            {
                revalidate(); repaint();
            }
        }


        public void columnAdded(TableColumnModelEvent e)
        {
            revalidate(); repaint();
        }

        public void columnRemoved(TableColumnModelEvent e)
        {
            revalidate(); repaint();
        }

        public void columnSelectionChanged(ListSelectionEvent e)
        {
        }

        public void columnMoved(TableColumnModelEvent e)
        {
            repaint();
        }

        public void columnMarginChanged(ChangeEvent e)
        {
            revalidate(); repaint();
        }
    }   

    private static abstract class TableColumnModels
    {
        private TableColumnModels()
        {
        }
        
        
        public static long minWidth(TableColumnModel columns)
        {
            long result = 0;
            
            for (Enumeration e = columns.getColumns(); e.hasMoreElements();)
                result += ((TableColumn)e.nextElement()).getMinWidth();
            
            return result;
        }
        
        public static long preferredWidth(TableColumnModel columns)
        {
            long result = 0;
            
            for (Enumeration e = columns.getColumns(); e.hasMoreElements();)
                result += ((TableColumn)e.nextElement()).getPreferredWidth();
            
            return result;
        }
        
        public static long maxWidth(TableColumnModel columns)
        {
            long result = 0;
            
            for (Enumeration e = columns.getColumns(); e.hasMoreElements();)
                result += ((TableColumn)e.nextElement()).getMaxWidth();
            
            return result;
        }
        
        public static int x(TableColumnModel columns, int index)
        {
            int result = 0;
            
            for (int i = 0; i < index; i++)
                result += columns.getColumn(i).getWidth();
            
            return result;
        }
        
        public static int x(TableColumnModel columns, TableColumn c)
        {
            int result = 0;
            
            TableColumn d;
            
            for (int count = columns.getColumnCount(), i = 0; i < count; i++)
            {
                if ((d = columns.getColumn(i)) == c) // equals?
                    break;

                result += d.getWidth();
            }
            
            return result;
        }
        
        
        public static int indexOf(TableColumnModel columns, TableColumn c)
        {
            for (int i = columns.getColumnCount() - 1; i >= 0; --i)
            {
                if (c.equals(columns.getColumn(i)))
                    return i;
            }
            
        return -1;
        }
    }
}