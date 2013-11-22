package finalstockex;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Twiz
 */
public class OrderBook extends JFrame
{

    private boolean runServer = true;
    public volatile static ArrayList<Trade> sellQueue, buyQueue;
    private Buyer b1, b2, b3;
    private Seller s0, s1, s2;
    Random rand;
    private JLabel orderBook;
    private JTable list;
    private JButton buyers, sellers;
    private JPanel orderPanel, traderPanel, marketPanel;
    private JScrollPane listPane;
    private DefaultTableModel model;
    public static volatile BuyerWindow buyerWindow;
    public static volatile SellerWindow sellerWindow;
    private UserInput userInput;
    private int count = 0;

    public OrderBook()
    {
        super("Welcome to the Order Book");

        buyerWindow = new BuyerWindow();
        sellerWindow = new SellerWindow();
        userInput = new UserInput();
        rand = new Random();
        sellQueue = new ArrayList<>();
        buyQueue = new ArrayList<>();
        createLabel();
        createTable();

        createButtons();
        createLayout();
        addListeners();
        setFrame();
        setVisible(true);

        this.setSellers();
        this.setBuyers();

        Thread theLoop = new Thread()
        {
            @Override
            public void run()
            {
                runSim();
            }
        };
        theLoop.start();
    }

    private void setFrame()
    {


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 500));
        setMaximumSize(new Dimension(800, 800));
        setPreferredSize(new Dimension(500, 500));
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        userInput.setVisible(true);
    }

    private void createLabel()
    {

        orderBook = new JLabel("Welcome to the Order Book");

    }

    private void createTable()
    {

        String[] columns =
        {
            "Stock", "Buyer", "Seller", "Quantity", "Price"
        };

        model = new DefaultTableModel();


        list = new JTable(model)
        {
            @Override
            public boolean isCellEditable(int rows, int column)
            {
                return false;
            }

            public boolean getScrollableTracksViewportWidth()
            {
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        
        list.setFillsViewportHeight(true);
        list.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < columns.length; i++)
        {
            model.addColumn(columns[i]);
        }

        list.setPreferredScrollableViewportSize(new Dimension(450, 350));
        list.setFillsViewportHeight(true);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellSelectionEnabled(true);
        list.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    // do some action if appropriate column
                }
            }
        });


        listPane = new JScrollPane(list);


    }

    private void createButtons()
    {

        buyers = new JButton("Buyers");
        sellers = new JButton("Sellers");

    }

    private void createLayout()
    {

        orderPanel = new JPanel();
        traderPanel = new JPanel();
        marketPanel = new JPanel();
        orderPanel.setLayout(new FlowLayout());
        orderPanel.add(orderBook);
        marketPanel.add(listPane);
        traderPanel.setLayout(new FlowLayout());
        traderPanel.add(buyers);
        traderPanel.add(sellers);
        add(orderPanel, BorderLayout.NORTH);
        add(marketPanel, BorderLayout.CENTER);
        add(traderPanel, BorderLayout.SOUTH);
    }

    private void addListeners()
    {

        buyers.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                buyerWindow.setVisible(true);
            }
        });

        sellers.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                sellerWindow.setVisible(true);

            }
        });
    }

    public void setSellers()
    {
        s0 = new Seller("Ian");
        s1 = new Seller("Tom");
        s2 = new Seller("Kim");
        s0.start();
        s1.start();
        s2.start();
    }

    public void setBuyers()
    {
        b1 = new Buyer("John");
        b2 = new Buyer("Terry");
        b3 = new Buyer("Pam");
        b1.start();
        b2.start();
        b3.start();
    }

    public void runSim()
    {

        while (runServer)
        {
            count++;
            this.marketPanel.repaint();
            for (int i = 0; i < sellQueue.size(); i++)
            {

                System.out.println(sellQueue.get(i).toString());
            }

            for (int i = 0; i < buyQueue.size(); i++)
            {
                System.out.println(buyQueue.get(i).toString());
            }

            for (int i = 0; i < sellQueue.size(); i++)
            {
                synchronized (this)
                {
                    for (int j = 0; j < buyQueue.size(); j++)
                    {
                        if (sellQueue.get(i).getType().equals(buyQueue.get(j).getType()))
                        {

                            System.out.printf("\nMatch found.\n%s\n%s\n", sellQueue.get(i).toString(), buyQueue.get(j).toString());
                            if (sellQueue.get(i).getPrice() <= buyQueue.get(j).getPrice())
                            {
                                String buyerName = buyQueue.get(j).getTraderName();
                                System.out.println("Buyer: " + buyerName);

                                switch (buyerName)
                                {
                                    case "John":
                                        if (sellQueue.get(i).getQuantity() < buyQueue.get(j).getQuantity())
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            b1.addBoughtGoods(new Stock(goods, sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by John");
                                            buyQueue.get(j).setQuantity(buyQueue.get(j).getQuantity() - sellQueue.get(i).getQuantity());
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            model.addRow(sellData);
                                            double amount = b1.getMyCash() - sellQueue.get(i).getPrice();
                                            b1.setMyCash(amount);

                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            sellQueue.get(i).setDeleteMe(true);
                                        } else if (sellQueue.get(i).getQuantity() > buyQueue.get(j).getQuantity())
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            b1.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by John");
                                            double amount = b1.getMyCash() - sellQueue.get(i).getPrice();
                                            b1.setMyCash(amount);
                                            sellQueue.get(i).setQuantity(sellQueue.get(i).getQuantity() - buyQueue.get(j).getQuantity());
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            buyQueue.get(j).setDeleteMe(true);
                                        } else
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            b1.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by John");
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            double amount = b1.getMyCash() - sellQueue.get(i).getPrice();
                                            b1.setMyCash(amount);
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            sellQueue.get(i).setDeleteMe(true);
                                            buyQueue.get(j).setDeleteMe(true);
                                        }
                                        break;
                                        case "USER":
                                        if (sellQueue.get(i).getQuantity() < buyQueue.get(j).getQuantity())
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            //b1.addBoughtGoods(new Stock(goods, sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by John");
                                            buyQueue.get(j).setQuantity(buyQueue.get(j).getQuantity() - sellQueue.get(i).getQuantity());
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            model.addRow(sellData);
                                            double amount = b1.getMyCash() - sellQueue.get(i).getPrice();
                                            b1.setMyCash(amount);

                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            sellQueue.get(i).setDeleteMe(true);
                                        } else if (sellQueue.get(i).getQuantity() > buyQueue.get(j).getQuantity())
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            //b1.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by John");
                                            //double amount = b1.getMyCash() - sellQueue.get(i).getPrice();
                                            //b1.setMyCash(amount);
                                            sellQueue.get(i).setQuantity(sellQueue.get(i).getQuantity() - buyQueue.get(j).getQuantity());
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            buyQueue.get(j).setDeleteMe(true);
                                        } else
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            //b1.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by John");
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            //double amount = b1.getMyCash() - sellQueue.get(i).getPrice();
                                            //b1.setMyCash(amount);
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            sellQueue.get(i).setDeleteMe(true);
                                            buyQueue.get(j).setDeleteMe(true);
                                        }
                                        break;
                                    case "Terry":
                                        if (sellQueue.get(i).getQuantity() < buyQueue.get(j).getQuantity())
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            b2.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by Terry");
                                            buyQueue.get(j).setQuantity(buyQueue.get(j).getQuantity() - sellQueue.get(i).getQuantity());
                                            double amount = b2.getMyCash() - sellQueue.get(i).getPrice();
                                            b2.setMyCash(amount);
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            sellQueue.get(i).setDeleteMe(true);
                                        } else if (sellQueue.get(i).getQuantity() > buyQueue.get(j).getQuantity())
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            b2.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by Terry");
                                            sellQueue.get(i).setQuantity(sellQueue.get(i).getQuantity() - buyQueue.get(j).getQuantity());
                                            double amount = b2.getMyCash() - sellQueue.get(i).getPrice();
                                            b2.setMyCash(amount);
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            buyQueue.get(j).setDeleteMe(true);
                                        } else
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            b2.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by Terry");
                                            sellQueue.get(i).setQuantity(sellQueue.get(i).getQuantity() - buyQueue.get(j).getQuantity());
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            double amount = b2.getMyCash() - sellQueue.get(i).getPrice();
                                            b2.setMyCash(amount);
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            sellQueue.get(i).setDeleteMe(true);
                                            buyQueue.get(j).setDeleteMe(true);
                                        }
                                        break;
                                    case "Pam":
                                        if (sellQueue.get(i).getQuantity() < buyQueue.get(j).getQuantity())
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            b3.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            buyQueue.get(j).setQuantity(buyQueue.get(j).getQuantity() - sellQueue.get(i).getQuantity());
                                            System.err.println(sellQueue.get(i) + " bought by Pam");
                                            double amount = b3.getMyCash() - sellQueue.get(i).getPrice();
                                            b3.setMyCash(amount);
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            sellQueue.get(i).setDeleteMe(true);
                                        } else if (sellQueue.get(i).getQuantity() > buyQueue.get(j).getQuantity())
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            b3.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by Pam");
                                            sellQueue.get(i).setQuantity(sellQueue.get(i).getQuantity() - buyQueue.get(j).getQuantity());
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            double amount = b3.getMyCash() - sellQueue.get(i).getPrice();
                                            b3.setMyCash(amount);
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "USER":
                                                    System.out.println("user gains " + sellQueue.get(i).getPrice());
                                            }
                                            buyQueue.get(j).setDeleteMe(true);
                                        } else
                                        {
                                            count = 0;
                                            String buyer = buyQueue.get(j).getTraderName();
                                            String seller = sellQueue.get(i).getTraderName();
                                            String goods = sellQueue.get(i).getType();
                                            String quant = String.valueOf(buyQueue.get(j).getQuantity());
                                            String price = String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()));
                                            b3.addBoughtGoods(new Stock(sellQueue.get(i).getType(), sellQueue.get(i).getQuantity(), sellQueue.get(i).getPrice()));
                                            System.err.println(sellQueue.get(i) + " bought by Pam");
                                            sellQueue.get(i).setQuantity(sellQueue.get(i).getQuantity() - buyQueue.get(j).getQuantity());
                                            String[] sellData =
                                            {
                                                goods, buyer, seller, quant, price
                                            };
                                            double amount = b3.getMyCash() - sellQueue.get(i).getPrice();
                                            b3.setMyCash(amount);
                                            model.addRow(sellData);
                                            switch (sellQueue.get(i).getTraderName())
                                            {
                                                case "Ian":
                                                    s0.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Tom":
                                                    s1.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                                case "Kim":
                                                    s2.addCash(sellQueue.get(i).getPrice());
                                                    break;
                                            }
                                            sellQueue.get(i).setDeleteMe(true);
                                            buyQueue.get(j).setDeleteMe(true);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
            }
            if (count >= 10)
            {
                s0.refreshSeller();
                s1.refreshSeller();
                s2.refreshSeller();
                b1.refreshBuyer();
                b2.refreshBuyer();
                b3.refreshBuyer();
                count = 0;
            }
            for (int i = 0; i < buyQueue.size(); i++)
            {
                if (buyQueue.get(i).isDeleteMe() || buyQueue.get(i).getQuantity() <= 0)
                {
                    buyQueue.remove(i);
                }
            }
            for (int i = 0; i < sellQueue.size(); i++)
            {
                if (sellQueue.get(i).isDeleteMe() || sellQueue.get(i).getQuantity() <= 0)
                {
                    sellQueue.remove(i);
                }
            }
            for (int i = 0; i < sellQueue.size(); i++)
            {
                sellQueue.get(i).decTTL();
                if (sellQueue.get(i).getTTL() <= 0)
                {
                    if (sellQueue.get(i).getPrice() > 20)
                    {
                        sellQueue.get(i).setPrice(sellQueue.get(i).getPrice() - 20);
                        sellQueue.get(i).resetTTL();
                        switch (sellQueue.get(i).getTraderName())
                        {
                            case "Ian":
                                String[] sellData0 =
                                {
                                    sellQueue.get(i).getTraderName()
                                        , String.valueOf(String.format("%.2f", s0.getMyCash()))
                                        , sellQueue.get(i).getType()
                                        , String.valueOf(sellQueue.get(i).getQuantity())
                                        , String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()))
                                };
                                sellerWindow.addRow(sellData0);
                                break;
                            case "Tom":
                                String[] sellData1 =
                                {
                                    sellQueue.get(i).getTraderName()
                                        , String.valueOf(String.format("%.2f", s1.getMyCash()))
                                        , sellQueue.get(i).getType()
                                        , String.valueOf(sellQueue.get(i).getQuantity())
                                        , String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()))
                                };
                                sellerWindow.addRow(sellData1);
                                break;
                            case "Kim":
                                String[] sellData2 =
                                {
                                    sellQueue.get(i).getTraderName()
                                        , String.valueOf(String.format("%.2f", s2.getMyCash()))
                                        , sellQueue.get(i).getType()
                                        , String.valueOf(sellQueue.get(i).getQuantity())
                                        , String.valueOf(String.format("%.2f", sellQueue.get(i).getPrice()))
                                };
                                sellerWindow.addRow(sellData2);
                                break;
                        }

                        System.out.println(sellQueue.get(i).getTraderName() + " lowers the price.");
                    }
                }
            }

            for (int i = 0; i < buyQueue.size(); i++)
            {
                buyQueue.get(i).decTTL();
                if (buyQueue.get(i).getTTL() <= 0)
                {
                    buyQueue.get(i).setPrice(buyQueue.get(i).getPrice() + 20);
                    buyQueue.get(i).resetTTL();
                    switch (buyQueue.get(i).getTraderName())
                    {
                        case "John":
                            String[] data1 =
                            {
                                buyQueue.get(i).getTraderName()
                                    , String.valueOf(String.format("%.2f", b1.getMyCash()))
                                    , buyQueue.get(i).getType()
                                    , String.valueOf(buyQueue.get(i).getQuantity())
                                    , String.valueOf(String.valueOf(buyQueue.get(i).getPrice()))
                            };
                            buyerWindow.addRow(data1);
                            break;
                        case "Terry":
                            String[] data2 =
                            {
                                buyQueue.get(i).getTraderName()
                                    , String.valueOf(String.format("%.2f", b2.getMyCash()))
                                    , buyQueue.get(i).getType()
                                    , String.valueOf(buyQueue.get(i).getQuantity())
                                    , String.valueOf(String.format("%.2f", buyQueue.get(i).getPrice()))
                            };
                            buyerWindow.addRow(data2);
                            break;
                        case "Pam":
                            String[] data3 =
                            {
                                buyQueue.get(i).getTraderName()
                                    , String.valueOf(String.format("%.2f", b3.getMyCash()))
                                    , buyQueue.get(i).getType()
                                    , String.valueOf(buyQueue.get(i).getQuantity())
                                    , String.valueOf(String.format("%.2f", buyQueue.get(i).getPrice()))
                            };
                            buyerWindow.addRow(data3);
                            break;
                    }
                    System.out.println(buyQueue.get(i).getTraderName() + " increases the bid.");
                }
            }

            System.out.println("Sale items = " + sellQueue.size());
            System.out.println("Buy items = " + buyQueue.size());


            try
            {
                Thread.sleep(5000);
            } catch (Exception e)
            {
                System.out.println(e);
            }
        }
    }
}
