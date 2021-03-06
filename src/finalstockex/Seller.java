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
public class Seller extends Thread
{
    private final String[] GOODS = {"VOD", "BARK", "GGLE", "SAMS", "SNY", "YTB", "DFG", "TRT", "HJU", "GGG" };
    private String name;
    private ArrayList<Stock>  myStock;
    Random rand = new Random();
    private double myCash;
    
    
    public Seller(String name)
    {
        this.name = name;
        myStock = new ArrayList<>();
        int randSel = rand.nextInt(GOODS.length);
        
        myStock.add(new Stock(GOODS[randSel]));
        myStock.add(new Stock(GOODS[randSel]));
        myCash = rand.nextInt(100) + 200;
    }
    
    public void makeSale()
    {
        for (int i = 0; i < myStock.size(); i++)
        {
            if(myStock.size() > 0)
            {
                OrderBook.sellQueue.add(new Trade("SELL", myStock.get(i).getName(), this.name, myStock.get(i).getQuantity(), myStock.get(i).getPrice()));
                String[] sellData = {this.name, String.valueOf(this.myCash), myStock.get(i).getName(), String.valueOf(myStock.get(i).getQuantity()), String.valueOf(myStock.get(i).getPrice())};
                OrderBook.sellerWindow.addRow(sellData);
                System.out.printf("\nselling %s at %.2f.\n", myStock.get(i).getName(), myStock.get(i).getPrice());
                myStock.remove(i);
            }
        }
    }
    
    public void refreshSeller()
    {
        int randSel = rand.nextInt(GOODS.length);
        myStock.add(new Stock(GOODS[randSel]));
        myStock.add(new Stock(GOODS[randSel]));
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            
            makeSale();
            
            try
            {
                Thread.sleep(rand.nextInt(2000) + 1000);
            } catch (Exception e)
            {
            }
        }
    }

    public double getMyCash()
    {
        return myCash;
    }

    public void setMyCash(double myCash)
    {
        this.myCash = myCash;
    }
    
    public void addCash(double cash)
    {
        myCash += cash;
    }
}
