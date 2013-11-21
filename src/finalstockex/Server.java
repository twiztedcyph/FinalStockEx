/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalstockex;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Twiz
 */
public class Server extends Thread
{
    private boolean runServer = true;
    public volatile static ArrayList<Trade> sellQueue, buyQueue;
    private Buyer b1, b2, b3;
    Random rand;
    
    public Server() 
    {
        rand = new Random();
        sellQueue = new ArrayList<>();
        buyQueue = new ArrayList<>();
    }
    
    public void setSellers()
    {
        Seller s0 = new Seller("Ian");
        Seller s1 = new Seller("Tom");
        Seller s2 = new Seller("Kim");
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
    
    @Override
    public void run()
    {
        setSellers();
        setBuyers();
        while(runServer)
        {
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
                for (int j = 0; j < buyQueue.size(); j++)
                {
                    if(sellQueue.get(i).getType().equals(buyQueue.get(j).getType()))
                    {
                        System.out.printf("\nMatch found.\n%s\n%s\n", sellQueue.get(i).toString(), buyQueue.get(j).toString());
                        if(sellQueue.get(i).getPrice() <= buyQueue.get(j).getPrice())
                        {
                            String buyerName = buyQueue.get(j).getTraderName();
                            System.out.println("Buyer: " + buyerName);
                            
                            switch(buyerName)
                            {
                                case "John":
                                    if(sellQueue.get(i).getQuantity() <= buyQueue.get(i).getQuantity())
                                    {
                                        b1.addBoughtGoods(new Stock(sellQueue.get(i).getType()
                                                , sellQueue.get(i).getQuantity()
                                                , sellQueue.get(i).getPrice()));
                                        System.out.println(sellQueue.get(i) + " bought by John");
                                        buyQueue.get(j).setQuantity(buyQueue.get(j).getQuantity() - sellQueue.get(i).getQuantity());
                                        sellQueue.remove(i);
                                    }else
                                    {
                                        b1.addBoughtGoods(new Stock(sellQueue.get(i).getType()
                                                , sellQueue.get(i).getQuantity()
                                                , sellQueue.get(i).getPrice()));
                                        System.out.println(sellQueue.get(i) + " bought by John");
                                        sellQueue.get(i).setQuantity(sellQueue.get(i).getQuantity() - buyQueue.get(j).getQuantity());
                                        buyQueue.remove(j);
                                    }
                                    break;
                                case "Terry":
                                    if(sellQueue.get(i).getQuantity() <= buyQueue.get(i).getQuantity())
                                    {
                                        b2.addBoughtGoods(new Stock(sellQueue.get(i).getType()
                                                , sellQueue.get(i).getQuantity()
                                                , sellQueue.get(i).getPrice()));
                                        System.out.println(sellQueue.get(i) + " bought by Terry");
                                        sellQueue.remove(i);
                                    }else
                                    {
                                        b1.addBoughtGoods(new Stock(sellQueue.get(i).getType()
                                                , sellQueue.get(i).getQuantity()
                                                , sellQueue.get(i).getPrice()));
                                        System.out.println(sellQueue.get(i) + " bought by Terry");
                                        sellQueue.get(i).setQuantity(sellQueue.get(i).getQuantity() - buyQueue.get(j).getQuantity());
                                        buyQueue.remove(j);
                                    }
                                    break;
                                case "Pam":
                                    if(sellQueue.get(i).getQuantity() <= buyQueue.get(i).getQuantity())
                                    {
                                        b3.addBoughtGoods(new Stock(sellQueue.get(i).getType()
                                                , sellQueue.get(i).getQuantity()
                                                , sellQueue.get(i).getPrice()));
                                        System.out.println(sellQueue.get(i) + " bought by Pam");
                                        sellQueue.remove(i);
                                    }else
                                    {
                                        b1.addBoughtGoods(new Stock(sellQueue.get(i).getType()
                                                , sellQueue.get(i).getQuantity()
                                                , sellQueue.get(i).getPrice()));
                                        System.out.println(sellQueue.get(i) + " bought by Pam");
                                        sellQueue.get(i).setQuantity(sellQueue.get(i).getQuantity() - buyQueue.get(j).getQuantity());
                                        buyQueue.remove(j);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
            
            for (int i = 0; i < sellQueue.size(); i++)
            {
                sellQueue.get(i).decTTL();
                if(sellQueue.get(i).getTTL() <= 0)
                {
                    int redAmount = rand.nextInt(20);
                    if(sellQueue.get(i).getPrice() > redAmount)
                    {
                        sellQueue.get(i).setPrice(sellQueue.get(i).getPrice() - 20);
                        sellQueue.get(i).resetTTL();
                        System.out.println(sellQueue.get(i).getTraderName() + " lowers the price.");
                    }
                }
            }
            
            for (int i = 0; i < buyQueue.size(); i++)
            {
                buyQueue.get(i).decTTL();
                if(buyQueue.get(i).getTTL() <= 0)
                {
                    buyQueue.get(i).setPrice(buyQueue.get(i).getPrice() + 20);
                    buyQueue.get(i).resetTTL();
                    System.out.println(buyQueue.get(i).getTraderName() + " increases the bid.");
                }
            }
            
            System.out.println("Sale items = " + sellQueue.size());
            System.out.println("Buy items = " + buyQueue.size());
            
            
            try
            {
                Thread.sleep(10000);
            } catch (Exception e)
            {
                System.out.println(e);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Get and set">
    public boolean isRunServer()
    {
        return runServer;
    }
    
    public void setRunServer(boolean runServer)
    {
        this.runServer = runServer;
    }
    
        
    public void addTrade(Trade newTrade)
    {
        this.sellQueue.add(newTrade);
    }
    //</editor-fold>
}
