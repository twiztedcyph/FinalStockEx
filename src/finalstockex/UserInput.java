/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalstockex;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserInput extends JFrame
{

    private JLabel welcomeLabel, stockLabel, quantityLabel, priceLabel;
    private JTextField stock, quantity, price;
    private JPanel center, p1, p2, p3, p4, p5;
    private JButton buy, sell;

    public UserInput()
    {
        super("Welcome to StockHeroes");
        createLabels();
        createTextBoxes();
        createButtons();
        createLayout();
        //addListeners();
        setFrame();
    }

    private void setFrame()
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(250, 300));
        setMaximumSize(new Dimension(800, 800));
        setPreferredSize(new Dimension(300, 300));
        setResizable(false);
        setLocationRelativeTo(null);
        setLocation(1250, 270);
        pack();
    }

    private void createButtons()
    {

        buy = new JButton("Buy");
        buy.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(stock.getText().length() > 0 && quantity.getText().length() > 0 && price.getText().length() > 0)
                {
                    OrderBook.buyQueue.add(new Trade("Buy", stock.getText(), "USER", Integer.parseInt(quantity.getText()), Double.parseDouble(price.getText())));
                    String[]  data = {"USER", "????", stock.getText(), quantity.getText(), price.getText()};
                    OrderBook.buyerWindow.addRow(data);
                }
            }
        });
        sell = new JButton("Sell");
        sell.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(stock.getText().length() > 0 && quantity.getText().length() > 0 && price.getText().length() > 0)
                {
                    System.out.println("hai");
                    OrderBook.sellQueue.add(new Trade("Buy", stock.getText(), "USER", Integer.parseInt(quantity.getText()), Double.parseDouble(price.getText())));
                    String[]  data = {"USER", "????", stock.getText(), quantity.getText(), price.getText()};
                    OrderBook.sellerWindow.addRow(data);
                }
            }
        });

    }

    private void createLabels()
    {

        stockLabel = new JLabel("Stock");
        quantityLabel = new JLabel("Quantity");
        welcomeLabel = new JLabel("Welcome to Stock Heroes!");
        priceLabel = new JLabel("Price");

    }

    private void createTextBoxes()
    {

        quantity = new JTextField(15);
        stock = new JTextField(15);
        price = new JTextField(15);
    }

    private void createLayout()
    {
        center = new JPanel();
        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();
        p4 = new JPanel();
        p5 = new JPanel();
        center.setLayout(new GridLayout(5, 1));
        p1.setLayout(new FlowLayout());
        p2.setLayout(new FlowLayout());
        p3.setLayout(new FlowLayout());
        p4.setLayout(new FlowLayout());
        p1.add(welcomeLabel);
        p2.add(stockLabel);
        p2.add(stock);

        p3.add(quantityLabel);
        p3.add(quantity);
        p4.add(priceLabel);
        p4.add(price);
        p5.add(buy);
        p5.add(sell);
        center.add(p1);
        center.add(p2);
        center.add(p3);
        center.add(p4);
        center.add(p5);
        add(center, BorderLayout.CENTER);

    }
}