/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalstockex;

/**
 *
 * @author Twiz
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class SellerWindow extends JFrame
{

    private JLabel buyers;
    private JTable table;
    private JScrollPane tablePane;
    private JPanel north, center;

    public SellerWindow()
    {

        super("Sellers");

        createLabel();
        createTable();
        createLayout();
        setFrame();

    }

    private void setFrame()
    {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(500, 500));
        setMaximumSize(new Dimension(800, 800));
        setPreferredSize(new Dimension(500, 500));
        setResizable(false);
        setLocation(1200, 250);
        pack();
    }

    private void createLabel()
    {

        buyers = new JLabel("Sellers");

    }

    private void createTable()
    {


        String[] columns =
        {
            "Name", "Company", "Quantity", "Price"
        };
        String[][] data =
        {
            {
                "Dragos Popescu", "Vodafone", "11", "200"
            },
        };
        table = new JTable(data, columns)
        {
            public boolean isCellEditable(int rows, int column)
            {
                return false;
            }
        };
        table.setPreferredScrollableViewportSize(new Dimension(450, 630));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);


        tablePane = new JScrollPane(table);
    }

    private void createLayout()
    {

        north = new JPanel();
        center = new JPanel();
        north.setLayout(new FlowLayout());
        north.add(buyers);
        center.add(tablePane);
        add(north, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

    }
}