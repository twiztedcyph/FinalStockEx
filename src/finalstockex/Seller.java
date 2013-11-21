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
    private final String[] GOODS = {"VOD", "BARK", "GGLE", "SAMS", "SNY", "YTB" };
    private String name;
    private ArrayList<Stock>  myStock;
    Random rand = new Random();
    
    public Seller(String name)
    {
        this.name = name;
        myStock = new ArrayList<>();
        int randSel = rand.nextInt(GOODS.length);
        
        myStock.add(new Stock(GOODS[randSel]));
        myStock.add(new Stock(GOODS[randSel]));
    }
    
    public void makeSale()
    {
        for (int i = 0; i < myStock.size(); i++)
        {
            if(myStock.size() > 0)
            {
                OrderBook.sellQueue.add(new Trade("SELL", myStock.get(i).getName(), this.name, myStock.get(i).getQuantity(), myStock.get(i).getPrice()));
                System.out.printf("selling %s at %.2f.", myStock.get(i).getName(), myStock.get(i).getPrice());
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
}
