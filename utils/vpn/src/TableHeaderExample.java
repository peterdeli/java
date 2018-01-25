import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import java.util.Vector;

class TableHeaderExample
{
    public static void main(String[] args)
    {
        DefaultTableModel data = new DefaultTableModel(0, 0);

        data.addColumn("ABC");
        data.addColumn("DEF");
        data.addColumn("GHI");
        data.addColumn("JKL");
        data.addColumn("MNO");
        data.addColumn("PQR");
        data.addColumn("STU");
        data.addColumn("VWX");

        for (int i = 0; i < 20; i++)
        {
            Vector row = new Vector();

            for (int k = 0; k < 10; k++)
                row.add(String.valueOf(i / (float)k));

            data.addRow(row);
        }

        TableHeader.XDefaultTableColumnModel columns = new TableHeader.XDefaultTableColumnModel();

        TableHeader.XTableColumn abc = new TableHeader.XTableColumn(0, 2);
        abc.setHeaderValue("ABC");

        TableHeader.XTableColumn ghi = new TableHeader.XTableColumn(2, 1);
        ghi.setHeaderValue("GHI");
        ghi.setResizable(false);

        TableHeader.XTableColumn jkl = new TableHeader.XTableColumn(3, 3);
        jkl.setHeaderValue("JKL");

        TableHeader.XTableColumn stu = new TableHeader.XTableColumn(6, 2);
        stu.setHeaderValue("STU");
        

        columns.addColumn(abc);
        columns.addColumn(new TableHeader.XTableColumn(1));
        columns.addBoundary(2);
        columns.addColumn(ghi);
        columns.addColumn(jkl);
        columns.addColumn(new TableHeader.XTableColumn(4));
        columns.addColumn(new TableHeader.XTableColumn(5));
        columns.addBoundary(6);
        columns.addColumn(stu);
        columns.addColumn(new TableHeader.XTableColumn(7));

        final JTable table = new JTable(data, columns)
        {
             protected void configureEnclosingScrollPane()
             {
                  if (getTableHeader() != null)
                      super.configureEnclosingScrollPane();
             }

             protected void unconfigureEnclosingScrollPane()
             {
                  if (getTableHeader() != null)
                      super.unconfigureEnclosingScrollPane();
             }
        };

        table.setTableHeader(null);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setShowGrid(false);
        table.setGridColor(Color.green.darker());

        final TableHeader header = new TableHeader(table);

        header.setFont(header.getFont().deriveFont(Font.BOLD).deriveFont(18.0f));
        
        JScrollPane pane = new JScrollPane(table);

        pane.setColumnHeaderView(header);


        JFrame f = new JFrame();

        f.setContentPane(pane);


        JPanel buttons = new JPanel();

        final JTextField rowHeight = new JTextField(5);

        rowHeight.setText(String.valueOf(table.getRowHeight()));

        rowHeight.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                table.setRowHeight(Integer.parseInt(rowHeight.getText()));
            }
        });

        final JTextField fontSize = new JTextField(5);

        fontSize.setText(String.valueOf(header.getFont().getSize()));


        fontSize.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                header.setFont(header.getFont().deriveFont((float)Integer.parseInt(fontSize.getText())));
            }
        });

        final JCheckBox resizing, reordering, disabled, horizontal, vertical;

        buttons.add(resizing = new JCheckBox("Resizing"));
        buttons.add(reordering = new JCheckBox("Reordering"));
        buttons.add(disabled = new JCheckBox("Disabled"));
        buttons.add(new JLabel("Font size"));
        buttons.add(fontSize);
        buttons.add(horizontal = new JCheckBox("Horizontal lines"));
        buttons.add(vertical = new JCheckBox("Vertical lines"));
        buttons.add(new JLabel("Row height"));
        buttons.add(rowHeight);

        resizing.setSelected(header.resizingEnabled());
        reordering.setSelected(header.reorderingEnabled());
        disabled.setSelected(!header.isEnabled());
        horizontal.setSelected(table.getShowHorizontalLines());
        vertical.setSelected(table.getShowVerticalLines());

        resizing.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                header.setResizingEnabled(resizing.isSelected());
            }
            });


        reordering.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                header.setReorderingEnabled(reordering.isSelected());
            }
            });

        disabled.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                header.setEnabled(!disabled.isSelected());
            }
            });

        horizontal.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                table.setShowHorizontalLines(horizontal.isSelected());
            }
            });
        
        vertical.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                table.setShowVerticalLines(vertical.isSelected());
            }
            });

        JFrame g = new JFrame();

        g.setContentPane(buttons);

        g.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        f.pack();
        f.setVisible(true);

        g.pack();
        g.setLocation(f.getX() + f.getWidth(), f.getY());
        g.setVisible(true);

    }
}