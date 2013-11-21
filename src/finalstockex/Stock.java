
package finalstockex;

import java.util.Random;


/**
 *
 * @author Twiz
 */
public class Stock
{
    private String name;
    private  int quantity;
    private double price;
    
    public Stock(String name)
    {
        Random rand = new Random();
        this.name = name;
        this.quantity = rand.nextInt(500);
        double decimal = (rand.nextInt(100) / 100.0);
        this.price = rand.nextInt(500) + decimal;
    }
    
    public Stock(String name, int quantity, double price)
    {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getQuantity()
    {
        return quantity;
    }
    
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    
    public double getPrice()
    {
        return price;
    }
    
    public void setPrice(double price)
    {
        this.price = price;
    }
    //</editor-fold>
}
