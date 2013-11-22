
package finalstockex;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Twiz
 */
public class Buyer extends Thread
{
    private final String[] GOODS = {"VOD", "BARK", "GGLE", "SAMS", "SNY", "YTB" };
    private String name;
    private Random rand;
    private ArrayList<String> wanted;
    private ArrayList<Stock> boughtGoods;
    
    public Buyer(String name)
    {
        rand = new Random();
        wanted = new ArrayList<>();
        int randSel = rand.nextInt(GOODS.length);
        wanted.add(GOODS[randSel]);
        randSel = rand.nextInt(GOODS.length);
        wanted.add(GOODS[randSel]);
        this.name = name;
        boughtGoods = new ArrayList<>();
    }
    
    public void placeBid()
    {
        for (int i = 0; i < wanted.size(); i++)
        {
            if(wanted.size() > 0)
            {
                double decimal = rand.nextInt(100) / 100.0;
                double price = rand.nextInt(500) + decimal;
                int quantity = rand.nextInt(200);
                OrderBook.buyQueue.add(new Trade("BUY", wanted.get(0), this.name, quantity, price));
                String[] sellData = {wanted.get(i), this.name, String.valueOf(quantity), String.valueOf(price)};
                OrderBook.buyerWindow.addRow(sellData);
                System.out.printf("\nBuying %s at %.2f.\n", wanted.get(0), price);
                wanted.remove(0);
            }
        }
//        if(wanted.size() > 0)
//        {
//            double decimal = rand.nextInt(100) / 100.0;
//            double price = rand.nextInt(500) + decimal;
//            int quantity = rand.nextInt(200);
//            OrderBook.buyQueue.add(new Trade("BUY", wanted.get(0), this.name, quantity, price));
//            System.out.printf("\nBuying %s at %.2f.\n", wanted.get(0), price);
//            wanted.remove(0);
//
//            decimal = rand.nextInt(100) / 100.0;
//            price = rand.nextInt(200) + decimal;
//            quantity = rand.nextInt(200);
//            OrderBook.buyQueue.add(new Trade("BUY", wanted.get(1), this.name, quantity, price));
//            System.out.printf("\nBuying %s at %.2f.\n", wanted.get(1), price);
//            wanted.remove(1);
//        }
    }
    
    public void refreshBuyer()
    {
        rand = new Random();
        wanted = new ArrayList<>();
        int randSel = rand.nextInt(GOODS.length);
        wanted.add(GOODS[randSel]);
        randSel = rand.nextInt(GOODS.length);
        wanted.add(GOODS[randSel]);
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                placeBid();
                Thread.sleep(rand.nextInt(2000) + 1000);
            } catch (Exception e)
            {
            }
        }
    }
    
    public void addBoughtGoods(Stock s)
    {
        System.out.println(this.name + " bought " + s.getName());
        this.boughtGoods.add(s);
    }
}
